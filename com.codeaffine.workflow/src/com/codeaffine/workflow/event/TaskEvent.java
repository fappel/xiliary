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
