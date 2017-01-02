/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.codeaffine.workflow.TaskList;
import com.codeaffine.workflow.definition.Task;
import com.codeaffine.workflow.event.TaskEvent;

public class TaskEventTest {

  private static final TaskList TASK_LIST = mock( TaskList.class );
  private static final Task TASK = mock( Task.class );

  @Test
  public void getTask() {
    TaskEvent event = new TaskEvent( TASK, TASK_LIST );

    Task actual = event.getTask();

    assertThat( actual ).isSameAs( TASK );
  }

  @Test
  public void getTaskList() {
    TaskEvent event = new TaskEvent( TASK, TASK_LIST );

    TaskList actual = event.getTaskList();

    assertThat( actual ).isSameAs( TASK_LIST );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsTask() {
    new TaskEvent( null, TASK_LIST );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsTaskList() {
    new TaskEvent( TASK, null );
  }
}
