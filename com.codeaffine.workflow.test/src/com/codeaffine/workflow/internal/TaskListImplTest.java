package com.codeaffine.workflow.internal;

import static com.codeaffine.workflow.internal.TaskListAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.workflow.TaskPredicate;
import com.codeaffine.workflow.Workflow;
import com.codeaffine.workflow.definition.Task;
import com.codeaffine.workflow.persistence.Memento;

public class TaskListImplTest {

  private TaskEventVerifier eventVerifier;
  private WorkflowImpl workflow;
  private TaskListImpl taskList;
  private Task task;

  @Before
  public void setUp() {
    eventVerifier = new TaskEventVerifier();
    taskList = new TaskListImpl( eventVerifier.getNotifier() );
    workflow = mock( WorkflowImpl.class );
    task = mock( Task.class );
  }

  @Test
  public void add() {
    taskList.add( task, workflow );

    assertThat( taskList )
      .isNotEmpty()
      .hasSize( 1 );
    eventVerifier.verifyTaskCreated( task, taskList );
  }

  @Test
  public void snapshot() {
    taskList.add( task, workflow );

    List<Task> actual = taskList.snapshot();

    assertThat( actual )
      .hasSize( 1 )
      .contains( task );
  }

  @Test
  public void snapshotWithFilter() {
    taskList.add( task, workflow );

    List<Task> actual = taskList.snapshot( new TaskPredicate() {
      @Override
      public boolean apply( Task input ) {
        return false;
      }
    } );

    assertThat( actual ).isEmpty();
  }

  @Test
  public void acquireAssignment() {
    taskList.add( task, workflow );

    UUID actual = taskList.acquireAssignment( task );

    assertThat( actual ).isNotNull();
    eventVerifier.verifyTaskAssigned( task, taskList );
  }

  @Test
  public void isAssigned() {
    taskList.add( task, workflow );
    taskList.acquireAssignment( task );

    boolean actual = taskList.isAssigned( task );

    assertThat( actual ).isTrue();
  }

  @Test
  public void isAssignedOfUnasignedTask() {
    taskList.add( task, workflow );

    boolean actual = taskList.isAssigned( task );

    assertThat( actual ).isFalse();
  }

  @Test
  public void isAssignedOfUnknownTask() {
    boolean actual = taskList.isAssigned( task );

    assertThat( actual ).isFalse();
  }

  @Test
  public void acquireAssignmentTwice() {
    taskList.add( task, workflow );

    UUID first = taskList.acquireAssignment( task );
    UUID second = taskList.acquireAssignment( task );

    assertThat( first ).isNotNull();
    assertThat( second ).isNull();
    eventVerifier.verifyTaskAssigned( task, taskList );
  }

  @Test
  public void acquireAssignmentOfUnknownTask() {
    taskList.add( task, workflow );

    UUID actual = taskList.acquireAssignment( mock( Task.class ) );

    assertThat( actual ).isNull();
    eventVerifier.verifyNoTaskAssigned();
  }

  @Test
  public void dropAssignment() {
    taskList.add( task, workflow );
    UUID token = taskList.acquireAssignment( task );

    taskList.dropAssigment( token );
    UUID actual = taskList.acquireAssignment( task );

    assertThat( actual ).isNotNull();
    eventVerifier.verifyTaskAssignmentDropped( task, taskList );
  }

  @Test
  public void dropAssignmentTwice() {
    taskList.add( task, workflow );
    UUID token = taskList.acquireAssignment( task );

    taskList.dropAssigment( token );
    taskList.dropAssigment( token );

    boolean actual = taskList.complete( token );
    assertThat( actual ).isFalse();
    eventVerifier.verifyTaskAssignmentDropped( task, taskList );
  }

  @Test
  public void dropUnknownAssignment() {
    taskList.add( task, workflow );
    UUID token = UUID.randomUUID();

    taskList.dropAssigment( token );

    eventVerifier.verifyNoTaskAssignmentDropped();
  }

  @Test
  public void complete() {
    taskList.add( task, workflow );
    UUID token = taskList.acquireAssignment( task );

    boolean actual = taskList.complete( token );

    assertThat( actual ).isTrue();
    verify( workflow ).continueWorkflow();
    eventVerifier.verifyTaskCompleted( task, taskList );
  }

  @Test
  public void completeTwice() {
    taskList.add( task, workflow );
    UUID token = taskList.acquireAssignment( task );
    taskList.complete( token );

    boolean actual = taskList.complete( token );

    assertThat( actual ).isFalse();
    verify( workflow, times( 1 ) ).continueWorkflow();
    eventVerifier.verifyTaskCompleted( task, taskList );
  }

  @Test
  public void completeWithUnknownToken() {
    taskList.add( task, workflow );
    UUID token = UUID.randomUUID();

    boolean actual = taskList.complete( token );

    assertThat( actual ).isFalse();
    verify( workflow, never() ).continueWorkflow();
    eventVerifier.verifyNoTaskCompleted();
  }

  @Test
  public void size() {
    assertThat( taskList )
      .isEmpty()
      .hasSize( 0 );
  }

  @Test
  public void isEmpty() {
    assertThat( taskList )
    .isEmpty()
    .hasSize( 0 );
  }

  @Test
  public void saveAndRestore() {
    taskList.add( task, workflow );
    UUID token = taskList.acquireAssignment( task );

    Memento memento = taskList.save();
    TaskListImpl newTaskList = new TaskListImpl( eventVerifier.getNotifier() );
    newTaskList.restore( memento );
    boolean assigned = newTaskList.isAssigned( task );
    boolean completed = newTaskList.complete( token );

    assertThat( assigned ).isTrue();
    assertThat( completed ).isTrue();
  }

  @Test
  public void changeAfterSave() {
    taskList.add( task, workflow );

    Memento memento = taskList.save();
    taskList.add( mock( Task.class ), workflow );
    TaskListImpl newTaskList = new TaskListImpl( eventVerifier.getNotifier() );
    newTaskList.restore( memento );

    assertThat( newTaskList ).hasSize( 1 );
  }

  @Test
  public void changeBeforeRestore() {
    taskList.add( task, workflow );

    Memento memento = taskList.save();
    taskList.acquireAssignment( mock( Task.class ) );
    TaskListImpl newTaskList = new TaskListImpl( eventVerifier.getNotifier() );
    newTaskList.add( mock( Task.class ), workflow );
    newTaskList.restore( memento );

    assertThat( newTaskList ).hasSize( 1 );
  }

  @Test
  public void getWorkflow() {
    taskList.add( task, workflow );

    Workflow actual = taskList.getWorkflow( task );

    assertThat( actual ).isSameAs( workflow );
  }

  @Test
  public void getWorkflowForTaskThatDoesNotExist() {
    Workflow actual = taskList.getWorkflow( task );

    assertThat( actual ).isNull();
  }
}