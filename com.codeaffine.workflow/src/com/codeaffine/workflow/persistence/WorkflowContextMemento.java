package com.codeaffine.workflow.persistence;

import java.util.Map;

import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.definition.VariableDeclaration;

public class WorkflowContextMemento {

  private Map<VariableDeclaration<?>, Object> content;

  public WorkflowContextMemento( Map<VariableDeclaration<?>, Object> content ) {
    this.content = content;
    content.remove( WorkflowContext.VARIABLE_CONTEXT );
    content.remove( WorkflowContext.VARIABLE_SERVICE );
    content.remove( WorkflowContext.VARIABLE_TASK_LIST );
  }

  public Map<VariableDeclaration<?>, Object> getContent() {
    return content;
  }
}
