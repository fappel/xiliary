package com.codeaffine.workflow.internal;

import org.assertj.core.api.AbstractAssert;

import com.codeaffine.workflow.TaskList;

public class TaskListAssert extends AbstractAssert<TaskListAssert, TaskList>{

  public static TaskListAssert assertThat( TaskList actual ) {
    return new TaskListAssert( actual );
  }

  public TaskListAssert( TaskList actual ) {
    super( actual, TaskListAssert.class );
  }

  public TaskListAssert hasSize( int expected ) {
    isNotNull();
    if( actual.size() != expected ) {
      failWithMessage( "Expected tasklist's size to be <%s> but was <%s>", expected, actual.size() );
    }
    return this;
  }

  public TaskListAssert isEmpty() {
    isNotNull();
    if( !actual.isEmpty() ) {
      failWithMessage( "Expected tasklist to be empty but it was not." );
    }
    return this;
  }

  public TaskListAssert isNotEmpty() {
    isNotNull();
    if( actual.isEmpty() ) {
      failWithMessage( "Expected tasklist not to be empty but it was." );
    }
    return this;
  }
}