package com.codeaffine.workflow;

public interface TaskListener {
  void taskCreated( TaskEvent event );
  void taskAssigned( TaskEvent event );
  void taskAssignmentDropped( TaskEvent event );
  void taskCompleted( TaskEvent event );
}