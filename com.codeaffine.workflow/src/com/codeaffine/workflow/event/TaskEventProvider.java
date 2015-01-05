package com.codeaffine.workflow.event;

public interface TaskEventProvider {
  void addTaskListener( TaskListener taskListener );
  void removeTaskListener( TaskListener taskListener );
}
