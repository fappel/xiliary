package com.codeaffine.workflow;

import java.util.UUID;

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