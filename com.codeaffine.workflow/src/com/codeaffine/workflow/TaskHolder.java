/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow;

import java.util.UUID;

import com.codeaffine.workflow.definition.Task;
import com.codeaffine.workflow.internal.WorkflowImpl;

public class TaskHolder {

  private final transient WorkflowImpl workflow;
  private final Task task;

  private UUID assignmentToken;

  public TaskHolder( Task task, Workflow workflow ) {
    this.workflow = ( WorkflowImpl )workflow;
    this.task = task;
  }

  public Task getTask() {
    return task;
  }

  public Workflow getWorkflow() {
    return workflow;
  }

  public void setAssigmentToken( UUID assignmentToken ) {
    this.assignmentToken = assignmentToken;
  }

  public UUID getAssignmentToken() {
    return assignmentToken;
  }

  public void complete() {
    workflow.continueWorkflow();
  }

  public boolean isAssigned() {
    return assignmentToken != null;
  }
}