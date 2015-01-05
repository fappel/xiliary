package com.codeaffine.workflow.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.codeaffine.workflow.Task;
import com.codeaffine.workflow.TaskEvent;
import com.codeaffine.workflow.TaskEventProvider;
import com.codeaffine.workflow.TaskList;
import com.codeaffine.workflow.TaskListener;

public class TaskEventNotifier implements TaskEventProvider {

  private final Set<TaskListener> listeners;

  public TaskEventNotifier() {
    listeners = new HashSet<TaskListener>();
  }

  @Override
  public void addTaskListener( TaskListener taskListener ) {
    synchronized( listeners ) {
      listeners.add( taskListener );
    }
  }

  @Override
  public void removeTaskListener( TaskListener taskListener ) {
    synchronized( listeners ) {
      listeners.remove( taskListener );
    }
  }

  public void notifyAboutTaskCreation( Task task, TaskList taskList  ) {
    TaskEvent event = new TaskEvent( task, taskList );
    for( TaskListener taskListener : getListeners() ) {
      taskListener.taskCreated( event );
    }
  }

  public void notifyAboutTaskAssignment( Task task, TaskList taskList ) {
    TaskEvent event = new TaskEvent( task, taskList );
    for( TaskListener taskListener : getListeners() ) {
      taskListener.taskAssigned( event );
    }
  }

  public void notifyAboutDropOfTaskAssignment( Task task, TaskList taskList ) {
    TaskEvent event = new TaskEvent( task, taskList );
    for( TaskListener taskListener : getListeners() ) {
      taskListener.taskAssignmentDropped( event );
    }
  }

  public void notifyAboutTaskCompletion( Task task, TaskList taskList ) {
    TaskEvent event = new TaskEvent( task, taskList );
    for( TaskListener taskListener : getListeners() ) {
      taskListener.taskCompleted( event );
    }
  }

  private Set<TaskListener> getListeners() {
    synchronized( listeners ) {
      return Collections.unmodifiableSet( listeners );
    }
  }
}