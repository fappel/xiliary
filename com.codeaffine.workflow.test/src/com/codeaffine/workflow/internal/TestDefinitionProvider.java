package com.codeaffine.workflow.internal;

import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.OPERATION_ID;

import com.codeaffine.workflow.definition.WorkflowDefinition;
import com.codeaffine.workflow.definition.WorkflowDefinitionProvider;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestTask;

public class TestDefinitionProvider implements WorkflowDefinitionProvider {

  @Override
  public void define( WorkflowDefinition definition ) {
    definition.setId( getClass().getName() );
    definition.addTask( OPERATION_ID, TestTask.class, null );
    definition.setStart( OPERATION_ID );
  }
}