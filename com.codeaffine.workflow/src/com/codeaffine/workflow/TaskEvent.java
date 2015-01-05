package com.codeaffine.workflow;

import static com.codeaffine.workflow.internal.ArgumentVerification.verifyNotNull;

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
