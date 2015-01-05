package com.codeaffine.workflow.internal;

import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.event.WorkflowContextEvent;
import com.codeaffine.workflow.event.WorkflowContextListener;

class NotifyHandler<T> {

  private final VariableDeclaration<T> declaration;
  private final WorkflowContextListener[] toNotify;
  private final T newVal;
  private final T oldVal;

  NotifyHandler( VariableDeclaration<T> declaration, T newVal, T oldVal, WorkflowContextListener ... toNotify ) {
    this.declaration = declaration;
    this.newVal = newVal;
    this.oldVal = oldVal;
    this.toNotify = toNotify;
  }

  void triggerVariableChanged() {
    if( hasChanged() ) {
      notifyListeners();
    }
  }

  private void notifyListeners() {
    WorkflowContextEvent<T> event = new WorkflowContextEvent<T>( declaration, newVal, oldVal );
    for( WorkflowContextListener workflowContextListener : toNotify ) {
      workflowContextListener.variableChanged( event );
    }
  }

  private boolean hasChanged() {
    return newVal == null && oldVal != null || ( newVal != null && !newVal.equals( oldVal ) );
  }
}