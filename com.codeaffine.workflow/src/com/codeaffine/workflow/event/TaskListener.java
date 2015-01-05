package com.codeaffine.workflow.event;

public interface TaskListener {
  void taskCreated( TaskEvent event );
  void taskAssigned( TaskEvent event );
  void taskAssignmentDropped( TaskEvent event );
  void taskCompleted( TaskEvent event );
}