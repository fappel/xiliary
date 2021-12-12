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
package com.codeaffine.workflow.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.codeaffine.workflow.TaskList;
import com.codeaffine.workflow.definition.Task;
import com.codeaffine.workflow.event.TaskEvent;
import com.codeaffine.workflow.event.TaskEventProvider;
import com.codeaffine.workflow.event.TaskListener;

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