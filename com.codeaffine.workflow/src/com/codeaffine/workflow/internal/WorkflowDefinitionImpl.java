/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.internal;

import static com.codeaffine.workflow.internal.ArgumentVerification.verifyNotNull;
import static com.codeaffine.workflow.internal.Successors.concat;
import static com.codeaffine.workflow.internal.Successors.toSuccessors;

import java.util.HashMap;
import java.util.Map;

import com.codeaffine.workflow.NodeDefinition;
import com.codeaffine.workflow.definition.Activity;
import com.codeaffine.workflow.definition.Decision;
import com.codeaffine.workflow.definition.Matcher;
import com.codeaffine.workflow.definition.Task;
import com.codeaffine.workflow.definition.WorkflowDefinition;

public class WorkflowDefinitionImpl implements WorkflowDefinition {

  private final Map<String, NodeDefinition> nodes;

  private String id;
  private String startNodeId;
  private Matcher matcher;

  public WorkflowDefinitionImpl() {
    this.nodes = new HashMap<String, NodeDefinition>();
  }

  @Override
  public void setId( String id ) {
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void addActivity( String nodeId, Class<? extends Activity> activity, String successor ) {
    verifyNotNull( nodeId, "nodeId" );
    verifyNotNull( activity, "activity" );

    nodes.put( nodeId, new NodeDefinition( nodeId, activity, toSuccessors( successor ) ) );
  }

  @Override
  public void addTask( String nodeId, Class<? extends Task> task, String successor ) {
    verifyNotNull( nodeId, "nodeId" );
    verifyNotNull( task, "task" );

    nodes.put( nodeId, new NodeDefinition( nodeId, task, toSuccessors( successor ) ) );
  }

  @Override
  public void addDecision(
    String nodeId, Class<? extends Decision> decision, String successor1, String successor2, String ... successors )
  {
    verifyNotNull( nodeId, "nodeId" );
    verifyNotNull( decision, "decision" );
    verifyNotNull( successor1, "successor1" );
    verifyNotNull( successor2, "successor2" );
    verifyNotNull( successors, "successors" );

    nodes.put( nodeId, new NodeDefinition( nodeId, decision, concat( successor1, successor2, successors ) ) );
  }

  NodeDefinition[] getNodeDefinitions() {
    return nodes.values().toArray( new NodeDefinition[ nodes.values().size() ] );
  }

  @Override
  public void setStart( String startNodeId ) {
    verifyNotNull( startNodeId, "startNodeId" );

    this.startNodeId = startNodeId;
  }

  String getStart() {
    return startNodeId;
  }

  @Override
  public void setMatcher( Matcher matcher ) {
    this.matcher = matcher;
  }

  public Matcher getMatcher() {
    return matcher;
  }

  public NodeDefinition getStartNode() {
    return getNode( startNodeId );
  }

  public NodeDefinition getNode( String nodeId ) {
    return nodes.get( nodeId );
  }
}