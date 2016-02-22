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
package com.codeaffine.workflow;

import com.codeaffine.workflow.event.FlowEventProvider;
import com.codeaffine.workflow.event.TaskEventProvider;
import com.codeaffine.workflow.persistence.Memento;

public interface WorkflowService extends FlowEventProvider, TaskEventProvider, WorkflowFactory, Scope {

  public static final String PROPERTY_AUTOSTART = "com.codeaffine.workflow.autostart";

  String[] getWorkflowDefinitionIds();
  TaskList getTaskList();
  Memento save();
  void restore( Memento memento );
}