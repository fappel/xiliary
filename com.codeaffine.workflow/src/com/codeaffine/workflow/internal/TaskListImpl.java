/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.codeaffine.workflow.TaskHolder;
import com.codeaffine.workflow.TaskList;
import com.codeaffine.workflow.TaskPredicate;
import com.codeaffine.workflow.Workflow;
import com.codeaffine.workflow.definition.Task;
import com.codeaffine.workflow.persistence.Memento;

public class TaskListImpl implements TaskList {

  private static final TaskPredicate APPLY_ALL = new TaskPredicate() {
    @Override
    public boolean apply( Task input ) {
      return true;
    }
  };

  private final List<TaskHolder> content;
  private final TaskEventNotifier notifier;

  public TaskListImpl( TaskEventNotifier notifier ) {
    this.notifier = notifier;
    this.content = new LinkedList<TaskHolder>();
  }

  public void add( Task task, WorkflowImpl workflow ) {
    synchronized( content ) {
      content.add( new TaskHolder( task, workflow ) );
    }
    notifier.notifyAboutTaskCreation( task, this );
  }

  @Override
  public List<Task> snapshot() {
    return snapshot( APPLY_ALL );
  }

  @Override
  public List<Task> snapshot( TaskPredicate filter ) {
    synchronized( content ) {
      return createSnapshot( filter );
    }
  }

  @Override
  public Workflow getWorkflow( Task task ) {
    synchronized( content ) {
      TaskHolder taskHolder = find( task );
      if( taskHolder != null ) {
        return taskHolder.getWorkflow();
      }
    }
    return null;
  }

  @Override
  public UUID acquireAssignment( Task task ) {
    UUID result;
    synchronized( content ) {
      result = acquire( task );
    }
    if( result != null ) {
      notifier.notifyAboutTaskAssignment( task, this );
    }
    return result;
  }

  @Override
  public void dropAssigment( UUID assignmentToken ) {
    Task droppedTask;
    synchronized( content ) {
      droppedTask = drop( assignmentToken );
    }
    if( droppedTask != null ) {
      notifier.notifyAboutDropOfTaskAssignment( droppedTask, this );
    }
  }

  @Override
  public boolean isAssigned( Task task ) {
    boolean result = false;
    synchronized( content ) {
      TaskHolder taskHolder = find( task );
      if( taskHolder != null ) {
        result = taskHolder.isAssigned();
      }
    }
    return result;
  }

  @Override
  public boolean complete( UUID assignmentToken ) {
    TaskHolder removed = remove( assignmentToken );
    boolean result = complete( removed );
    if( result ) {
      notifier.notifyAboutTaskCompletion( removed.getTask(), this );
    }
    return result;
  }

  @Override
  public int size() {
    synchronized( content ) {
      return content.size();
    }
  }

  @Override
  public boolean isEmpty() {
    synchronized( content ) {
      return content.isEmpty();
    }
  }

  public Memento save() {
    ArrayList<TaskHolder> copy = new ArrayList<TaskHolder>();
    copy.addAll( content );
    return new Memento( copy );
  }

  public void restore( Memento memento ) {
    content.clear();
    content.addAll( memento.getContent() );
  }

  private TaskHolder find( Task task ) {
    for( TaskHolder taskHolder : content ) {
      if( taskHolder.getTask() == task ) {
        return taskHolder;
      }
    }
    return null;
  }

  private TaskHolder find( UUID assigmentId ) {
    for( TaskHolder taskHolder : content ) {
      if( assigmentId.equals( taskHolder.getAssignmentToken() ) ) {
        return taskHolder;
      }
    }
    return null;
  }

  private List<Task> createSnapshot( TaskPredicate filter ) {
    ArrayList<Task> result = new ArrayList<Task>();
    Iterator<TaskHolder> iterator = content.iterator();
    while( iterator.hasNext() ) {
      TaskHolder taskHolder = iterator.next();
      if( filter.apply( taskHolder.getTask() ) ) {
        result.add( taskHolder.getTask() );
      }
    }
    return result;
  }

  private UUID acquire( Task task ) {
    UUID result = null;
    TaskHolder taskHolder = find( task );
    if( taskHolder != null && !taskHolder.isAssigned() ) {
      result = UUID.randomUUID();
      taskHolder.setAssigmentToken( result );
    }
    return result;
  }

  private Task drop( UUID assignmentToken ) {
    TaskHolder taskHolder = find( assignmentToken );
    if( taskHolder != null ) {
      taskHolder.setAssigmentToken( null );
      return taskHolder.getTask();
    }
    return null;
  }

  private TaskHolder remove( UUID assignmentToken ) {
    TaskHolder taskHolder;
    synchronized( content ) {
      taskHolder = find( assignmentToken );
      content.remove( taskHolder );
    }
    return taskHolder;
  }

  private static boolean complete( TaskHolder taskHolder ) {
    boolean result = false;
    if( taskHolder != null ) {
      taskHolder.complete();
      result = true;
    }
    return result;
  }
}