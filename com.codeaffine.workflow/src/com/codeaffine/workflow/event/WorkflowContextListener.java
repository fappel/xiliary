package com.codeaffine.workflow.event;

public interface WorkflowContextListener {
  void variableChanged( WorkflowContextEvent<?> event );
}
