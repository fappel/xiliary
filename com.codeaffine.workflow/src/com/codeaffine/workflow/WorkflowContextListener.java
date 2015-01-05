package com.codeaffine.workflow;

public interface WorkflowContextListener {
  void variableChanged( WorkflowContextEvent<?> event );
}
