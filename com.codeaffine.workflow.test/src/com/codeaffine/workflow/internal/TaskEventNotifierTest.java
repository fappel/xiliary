package com.codeaffine.workflow.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.codeaffine.workflow.definition.Task;
import com.codeaffine.workflow.event.TaskEvent;
import com.codeaffine.workflow.event.TaskListener;

public class TaskEventNotifierTest {

  private static final TaskListImpl TASK_LIST = mock( TaskListImpl.class );
  private static final Task TASK = mock( Task.class );

  private ArgumentCaptor<TaskEvent> eventCaptor;
  private TaskEventNotifier notifier;
  private TaskListener listener;

  @Before
  public void setUp() {
    notifier = new TaskEventNotifier();
    listener = mock( TaskListener.class );
    notifier.addTaskListener( listener );
    eventCaptor = forClass( TaskEvent.class );
  }

  @Test
  public void testNotifyAboutTaskCreation() {
    notifier.notifyAboutTaskCreation( TASK, TASK_LIST );

    verify( listener ).taskCreated( eventCaptor.capture() );
    assertThat( eventCaptor.getValue().getTask() ).isSameAs( TASK );
    assertThat( eventCaptor.getValue().getTaskList() ).isSameAs( TASK_LIST );
  }

  @Test
  public void testNotifyAboutTaskAssignment() {
    notifier.notifyAboutTaskAssignment( TASK, TASK_LIST );

    verify( listener ).taskAssigned( eventCaptor.capture() );
    assertThat( eventCaptor.getValue().getTask() ).isSameAs( TASK );
    assertThat( eventCaptor.getValue().getTaskList() ).isSameAs( TASK_LIST );
  }

  @Test
  public void testNotifyAboutDropOfTaskAssignment() {
    notifier.notifyAboutDropOfTaskAssignment( TASK, TASK_LIST );

    verify( listener ).taskAssignmentDropped( eventCaptor.capture() );
    assertThat( eventCaptor.getValue().getTask() ).isSameAs( TASK );
    assertThat( eventCaptor.getValue().getTaskList() ).isSameAs( TASK_LIST );
  }

  @Test
  public void testNotifyAboutTaskCompletion() {
    notifier.notifyAboutTaskCompletion( TASK, TASK_LIST );

    verify( listener ).taskCompleted( eventCaptor.capture() );
    assertThat( eventCaptor.getValue().getTask() ).isSameAs( TASK );
    assertThat( eventCaptor.getValue().getTaskList() ).isSameAs( TASK_LIST );
  }

  @Test
  public void removeTaskListener() {
    notifier.removeTaskListener( listener );

    notifier.notifyAboutTaskCreation( TASK, TASK_LIST );

    verify( listener, never() ).taskCreated( any( TaskEvent.class ) );
  }
}