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