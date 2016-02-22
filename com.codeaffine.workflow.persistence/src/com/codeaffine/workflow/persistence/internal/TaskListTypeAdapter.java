/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.persistence.internal;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.TaskHolder;
import com.codeaffine.workflow.Workflow;
import com.codeaffine.workflow.WorkflowFactory;
import com.codeaffine.workflow.definition.Task;
import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.persistence.FlowProcessorMemento;
import com.codeaffine.workflow.persistence.WorkflowAdapter;
import com.codeaffine.workflow.persistence.WorkflowContextMemento;
import com.codeaffine.workflow.persistence.WorkflowMemento;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class TaskListTypeAdapter
  implements JsonDeserializer<List<TaskHolder>>, JsonSerializer<List<TaskHolder>>
{

  private static final int ASSIGNMENT_TOKEN_INDEX = 0;
  private static final int TASK_TYPE_INDEX = 1;
  private static final int WORKFLOW_DEFINITION_ID_INDEX = 2;

  private final WorkflowFactory workflowFactory;
  private final ServiceScopedVariableBuffer workflowServiceBuffer;
  private final NodeLoader nodeLoader;

  public static Type getType() {
    return new TypeToken<List<TaskHolder>>() {}.getType();
  }

  public TaskListTypeAdapter( WorkflowFactory workflowFactory, NodeLoader nodeLoader ) {
    this.workflowServiceBuffer = new ServiceScopedVariableBuffer();
    this.workflowFactory = workflowFactory;
    this.nodeLoader = nodeLoader;
  }

  @Override
  public JsonElement serialize( List<TaskHolder> src, Type typeOfSrc, JsonSerializationContext context ) {
    JsonArray result = new JsonArray();
    for( TaskHolder taskHolder : src ) {
      WorkflowAdapter adapter = new WorkflowAdapter( taskHolder.getWorkflow() );
      WorkflowMemento workflowMemento = adapter.save();

      JsonArray entry = new JsonArray();
      entry.add( context.serialize( taskHolder.getAssignmentToken() ) );
      entry.add( context.serialize( taskHolder.getTask().getClass() ) );
      entry.add( context.serialize( adapter.getDefinitionId() ) );
      entry.add( context.serialize( workflowMemento.getContextMemento().getContent(), WorkflowContextTypeAdapter.getType() ) );
      entry.add( context.serialize( workflowMemento.getFlowProcessorMemento(), FlowProcessorMemento.class ) );
      result.add( entry );
    }
    return result;
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public List<TaskHolder> deserialize( JsonElement json, Type typeOfT, JsonDeserializationContext context )
    throws JsonParseException
  {
    List<TaskHolder> result = new LinkedList<TaskHolder>();
    JsonArray entries = json.getAsJsonArray();
    for( int i = 0; i < entries.size(); i++ ) {
      JsonArray entry = entries.get( i ).getAsJsonArray();

      WorkflowContextMemento contextMemento = new WorkflowContextMemento( ( Map<VariableDeclaration<?>, Object> )context.deserialize( entry.get( 3 ), WorkflowContextTypeAdapter.getType() ) );
      FlowProcessorMemento flowProcessorMemento = context.deserialize( entry.get( 4 ), FlowProcessorMemento.class );
      WorkflowMemento workflowMemento = new WorkflowMemento( flowProcessorMemento, contextMemento );

      result.add( restorTaskHolder( context, entry, workflowMemento ) );
    }
    return result;
  }

  private TaskHolder restorTaskHolder( JsonDeserializationContext context, JsonArray entry, WorkflowMemento workflowMemento ) {
    Workflow workflow = restoreWorkflow( context, entry, workflowMemento );
    Task task = restoreTask( context, entry, workflow );
    TaskHolder result = new TaskHolder( task, workflow );
    result.setAssigmentToken( restoreAssignmentToken( context, entry ) );
    return result;
  }

  private Workflow restoreWorkflow( JsonDeserializationContext context, JsonArray entryl, WorkflowMemento memento ) {
    String workflowId = context.deserialize( entryl.get( WORKFLOW_DEFINITION_ID_INDEX ), String.class );
    Workflow result = workflowFactory.create( workflowId );
    workflowServiceBuffer.buffer( result );
    new WorkflowAdapter( result ).restore( memento );
    workflowServiceBuffer.restore( result );
    return result;
  }

  private Task restoreTask( JsonDeserializationContext context, JsonArray jsonEntry, Workflow workflow ) {
    Class<?> taskType = context.deserialize( jsonEntry.get( TASK_TYPE_INDEX ), Class.class );
    return ( Task )nodeLoader.load( taskType, workflow.getContext() );
  }

  private static UUID restoreAssignmentToken( JsonDeserializationContext context, JsonArray entry ) {
    return context.deserialize( entry.get( ASSIGNMENT_TOKEN_INDEX ), UUID.class );
  }
}