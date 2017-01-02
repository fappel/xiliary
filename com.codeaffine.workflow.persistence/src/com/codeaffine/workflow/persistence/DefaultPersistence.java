/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.TaskHolder;
import com.codeaffine.workflow.WorkflowFactory;
import com.codeaffine.workflow.WorkflowService;
import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.persistence.internal.ClassTypeAdapter;
import com.codeaffine.workflow.persistence.internal.TaskListTypeAdapter;
import com.codeaffine.workflow.persistence.internal.VariableDeclarationTypeAdapter;
import com.codeaffine.workflow.persistence.internal.WorkflowContextTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DefaultPersistence implements Persistence {

  private final Gson gson;

  public DefaultPersistence( WorkflowService workflowService ) {
    this( workflowService, getNodeLoader( workflowService ), getClassFinder( workflowService ) );
  }

  public DefaultPersistence( WorkflowFactory workflowFactory, NodeLoader nodeLoader, ClassFinder classFinder ){
    gson = new GsonBuilder()
      .registerTypeHierarchyAdapter( Class.class, new ClassTypeAdapter( classFinder ) )
      .registerTypeAdapter( VariableDeclarationTypeAdapter.getType(), new VariableDeclarationTypeAdapter( classFinder ) )
      .registerTypeAdapter( WorkflowContextTypeAdapter.getType(), new WorkflowContextTypeAdapter() )
      .registerTypeAdapter( TaskListTypeAdapter.getType(), new TaskListTypeAdapter( workflowFactory, nodeLoader ) )
      .create();
  }

  @Override
  public Object serialize( Memento memento ) {
    return serializeTaskList( memento );
  }

  @Override
  public Memento deserialize( Object data ) {
    return deserializeTaskList( ( String )data );
  }

  @Override
  public void serialize( Memento memento, OutputStream out ) {
    JsonWriter writer = new JsonWriter( new OutputStreamWriter( out ) );
    gson.toJson( memento.getContent(), TaskListTypeAdapter.getType(), writer );
    try {
      writer.flush();
    } catch( IOException e ) {
      e.printStackTrace();
    }
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public Memento deserialize( InputStream in ) {
    Object fromJson = gson.fromJson( new JsonReader( new InputStreamReader( in ) ), TaskListTypeAdapter.getType() );
    return new Memento( ( List<TaskHolder> )fromJson );
  }

  String serializeWorkflowContext( WorkflowContextMemento memento ) {
    return gson.toJson( memento.getContent(), WorkflowContextTypeAdapter.getType() );
  }

  @SuppressWarnings( "unchecked" )
  WorkflowContextMemento deserializeWorkflowContext( String json ) {
    Object fromJson = gson.fromJson( json, WorkflowContextTypeAdapter.getType() );
    return new WorkflowContextMemento( ( Map<VariableDeclaration<?>, Object> )fromJson );
  }

  String serializeFlowProcessor( FlowProcessorMemento memento ) {
    return gson.toJson( memento, FlowProcessorMemento.class );
  }

  FlowProcessorMemento deserializeFlowProcessor( String json ) {
    return gson.fromJson( json, FlowProcessorMemento.class );
  }

  String serializeTaskList( Memento memento ) {
    return gson.toJson( memento.getContent(), TaskListTypeAdapter.getType() );
  }

  @SuppressWarnings( "unchecked" )
  Memento deserializeTaskList( String json ) {
    Object fromJson = gson.fromJson( json, TaskListTypeAdapter.getType() );
    return new Memento( ( List<TaskHolder> )fromJson );
  }

  private static NodeLoader getNodeLoader( WorkflowService workflowService ) {
    return new WorkflowServiceAdapter( workflowService ).getNodeLoader();
  }

  private static ClassFinder getClassFinder( WorkflowService workflowService ) {
    return new WorkflowServiceAdapter( workflowService ).getClassFinder();
  }
}