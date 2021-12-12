/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.event;

import static com.codeaffine.workflow.internal.ArgumentVerification.verifyNotNull;

import com.codeaffine.workflow.TaskList;
import com.codeaffine.workflow.definition.Task;

public class TaskEvent {

  private final TaskList taskList;
  private final Task task;

  public TaskEvent( Task task, TaskList taskList ) {
    verifyNotNull( task, "task" );
    verifyNotNull( taskList, "taskListImpl" );

    this.task = task;
    this.taskList = taskList;
  }

  public Task getTask() {
    return task;
  }

  public TaskList getTaskList() {
    return taskList;
  }
}
