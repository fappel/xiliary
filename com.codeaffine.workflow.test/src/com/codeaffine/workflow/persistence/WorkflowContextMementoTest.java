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
package com.codeaffine.workflow.persistence;

import static com.codeaffine.workflow.WorkflowContext.VARIABLE_CONTEXT;
import static com.codeaffine.workflow.WorkflowContext.VARIABLE_SERVICE;
import static com.codeaffine.workflow.WorkflowContext.VARIABLE_TASK_LIST;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.OPERATION_ID;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Test;

import com.codeaffine.workflow.Workflow;
import com.codeaffine.workflow.WorkflowService;
import com.codeaffine.workflow.WorkflowServiceFactory;
import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.definition.WorkflowDefinition;
import com.codeaffine.workflow.definition.WorkflowDefinitionProvider;
import com.codeaffine.workflow.internal.WorkflowContextImpl;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestActivity;

public class WorkflowContextMementoTest {

  static class DefinitionProvider implements WorkflowDefinitionProvider {

    private static final String ID = "id";

    @Override
    public void define( WorkflowDefinition definition ) {
      definition.setId( ID );
      definition.addActivity( OPERATION_ID, TestActivity.class, null );
      definition.setStart( OPERATION_ID );
    }
  }

  @Test
  public void getContent() {
    WorkflowService service = new WorkflowServiceFactory( new DefinitionProvider() ).createWorkflowService();
    Workflow workflow = service.create( DefinitionProvider.ID );
    WorkflowContextImpl context = ( WorkflowContextImpl )workflow.getContext();
    WorkflowContextMemento memento = context.save();

    Map<VariableDeclaration<?>, Object> actual = memento.getContent();

    assertThat( actual.get( VARIABLE_SERVICE ) ).isNull();
    assertThat( actual.get( VARIABLE_CONTEXT ) ).isNull();
    assertThat( actual.get( VARIABLE_TASK_LIST ) ).isNull();
  }

  @Test
  public void getContentIfWorkflowServiceIsNotDefined() {
    WorkflowService service = new WorkflowServiceFactory( new DefinitionProvider() ).createWorkflowService();
    Workflow workflow = service.create( DefinitionProvider.ID );
    WorkflowContextImpl context = ( WorkflowContextImpl )workflow.getContext();
    context.defineVariable( VARIABLE_CONTEXT, null );
    WorkflowContextMemento memento = context.save();

    Map<VariableDeclaration<?>, Object> actual = memento.getContent();

    assertThat( actual.get( VARIABLE_SERVICE ) ).isNull();
    assertThat( actual.get( VARIABLE_CONTEXT ) ).isNull();
    assertThat( actual.get( VARIABLE_TASK_LIST ) ).isNull();
  }
}