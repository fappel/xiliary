package com.codeaffine.workflow;

public interface TaskEventProvider {
  void addTaskListener( TaskListener taskListener );
  void removeTaskListener( TaskListener taskListener );
}
