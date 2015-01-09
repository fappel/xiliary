package com.codeaffine.workflow.persistence.internal;

import static com.codeaffine.workflow.WorkflowContext.VARIABLE_SERVICE;

import java.util.HashMap;

import com.codeaffine.workflow.Workflow;
import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.WorkflowService;
import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.persistence.WorkflowServiceAdapter;

class ServiceScopedVariableBuffer {

  private final HashMap<VariableDeclaration<Object>, Object> buffer;

  ServiceScopedVariableBuffer() {
    this.buffer = new HashMap<VariableDeclaration<Object>, Object>();
  }

  void buffer( Workflow workflow ) {
    WorkflowContext context = workflow.getContext();
    if( context.hasVariableDefinition( VARIABLE_SERVICE ) ) {
      VariableDeclaration<Object>[] declarations = getDeclarations( context );
      for( VariableDeclaration<Object> declaration : declarations ) {
        buffer.put( declaration, context.getVariableValue( declaration ) );
      }
    }
  }

  void restore( Workflow workflow ) {
    WorkflowContext context = workflow.getContext();
    if( !buffer.isEmpty() ) {
      for( VariableDeclaration<Object> declaration : buffer.keySet() ) {
        context.defineVariable( declaration, buffer.get( declaration ) );
      }
    }
    buffer.clear();
  }

  @SuppressWarnings("unchecked")
  private static VariableDeclaration<Object>[] getDeclarations( WorkflowContext context ) {
    WorkflowService service = context.getVariableValue( VARIABLE_SERVICE );
    WorkflowServiceAdapter serviceAdapter = new WorkflowServiceAdapter( service );
    return ( VariableDeclaration<Object>[] )serviceAdapter.getVariableDeclarations();
  }
}