package com.codeaffine.workflow.persistence.internal;

import static com.codeaffine.workflow.WorkflowContext.VARIABLE_SERVICE;

import com.codeaffine.workflow.Workflow;
import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.WorkflowService;

class WorkflowServiceBuffer {

  private WorkflowService buffer;

  void buffer( Workflow workflow ) {
    WorkflowContext context = workflow.getContext();
    if( context.hasVariableDefinition( VARIABLE_SERVICE ) ) {
      buffer = context.getVariableValue( VARIABLE_SERVICE );
    }
  }

  void restore( Workflow workflow ) {
    WorkflowContext context = workflow.getContext();
    if( buffer != null ) {
      context.defineVariable( VARIABLE_SERVICE, buffer );
    }
    buffer = null;
  }
}