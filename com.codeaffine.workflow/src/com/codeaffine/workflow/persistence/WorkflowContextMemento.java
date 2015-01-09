package com.codeaffine.workflow.persistence;

import static com.codeaffine.workflow.WorkflowContext.VARIABLE_SERVICE;

import java.util.Map;

import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.WorkflowService;
import com.codeaffine.workflow.definition.VariableDeclaration;

public class WorkflowContextMemento {

  private final Map<VariableDeclaration<?>, Object> content;

  public WorkflowContextMemento( Map<VariableDeclaration<?>, Object> content ) {
    this.content = content;
    content.remove( WorkflowContext.VARIABLE_CONTEXT );
    content.remove( WorkflowContext.VARIABLE_TASK_LIST );
    removeServiceScopedVariables( content );
  }

  public Map<VariableDeclaration<?>, Object> getContent() {
    return content;
  }

  private static void removeServiceScopedVariables( Map<VariableDeclaration<?>, Object> content ) {
    WorkflowService service = ( WorkflowService )content.get( VARIABLE_SERVICE );
    if( service != null ) {
      WorkflowServiceAdapter serviceAdapter = new WorkflowServiceAdapter( service );
      VariableDeclaration<?>[] variableDeclarations = serviceAdapter.getVariableDeclarations();
      for( VariableDeclaration<?> variableDeclaration : variableDeclarations ) {
        content.remove( variableDeclaration );
      }
    }
  }
}