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
package com.codeaffine.workflow;

import static com.codeaffine.workflow.WorkflowContexts.prefix;

import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.event.WorkflowContextListener;
import com.codeaffine.workflow.internal.ScopeContext;


public interface WorkflowContext extends ScopeContext {

  public static final VariableDeclaration<WorkflowContext> VARIABLE_CONTEXT
    = new VariableDeclaration<WorkflowContext>( prefix( "workflowContext" ), WorkflowContext.class );
  public static final VariableDeclaration<WorkflowService> VARIABLE_SERVICE
    = new VariableDeclaration<WorkflowService>( prefix( "workflowService" ), WorkflowService.class );
  public static final VariableDeclaration<TaskList> VARIABLE_TASK_LIST
    = new VariableDeclaration<TaskList>( prefix( "taskList" ), TaskList.class );

  @Override
  boolean hasVariableDefinition( VariableDeclaration<?> declaration );
  @Override
  <T> T getVariableValue( VariableDeclaration<T> declaration );
  @Override
  VariableDeclaration<?>[] getVariableDeclarations();

  void addWorkflowContextListener( WorkflowContextListener workflowContextListener );
  void removeWorkflowContextListener( WorkflowContextListener workflowContextListener );
}