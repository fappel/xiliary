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
package com.codeaffine.workflow.internal;

import java.util.Map;

import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.Workflow;
import com.codeaffine.workflow.WorkflowFactory;
import com.codeaffine.workflow.definition.VariableDeclaration;

class WorkflowFactoryImpl implements WorkflowFactory {

  private final Map<String, WorkflowDefinitionImpl> definitions;
  private final FlowEventNotifier notifier;
  private final ScopeImpl serviceScope;
  private final TaskListImpl taskList;
  private final NodeLoader nodeLoader;

  WorkflowFactoryImpl(
    ScopeImpl serviceScope,
    Map<String, WorkflowDefinitionImpl> definitions,
    TaskListImpl taskList,
    FlowEventNotifier notifier,
    NodeLoader nodeLoader )
  {
      this.serviceScope = serviceScope;
      this.definitions = definitions;
      this.taskList = taskList;
      this.notifier = notifier;
      this.nodeLoader = nodeLoader;
  }

  @Override
  public Workflow create( String definitionId ) {
    if( definitions.get( definitionId ) != null ) {
      return doCreate( definitionId );
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private WorkflowImpl doCreate( String id ) {
    WorkflowImpl result = new WorkflowImpl( definitions.get( id ), taskList, notifier, nodeLoader );
    VariableDeclaration<?>[] variableDeclarations = serviceScope.getVariableDeclarations();
    VariableDeclaration<Object>[] declarations = ( VariableDeclaration<Object>[] )variableDeclarations;
    for( VariableDeclaration<Object> declaration : declarations ) {
      result.defineVariable( declaration, serviceScope.getVariableValue( declaration ) );
    }
    return result;
  }
}