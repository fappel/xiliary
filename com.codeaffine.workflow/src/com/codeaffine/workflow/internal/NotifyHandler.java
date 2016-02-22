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