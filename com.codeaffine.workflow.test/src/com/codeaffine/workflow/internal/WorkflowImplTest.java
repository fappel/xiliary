package com.codeaffine.workflow.internal;

import static com.codeaffine.workflow.internal.TaskListAssert.assertThat;
import static com.codeaffine.workflow.test.util.FlowEventLog.$;
import static com.codeaffine.workflow.test.util.FlowEventLog.expectedLog;
import static com.codeaffine.workflow.test.util.FlowEventLog.registerFlowEventLog;
import static com.codeaffine.workflow.test.util.FlowEventLogEntry.EventType.ON_NODE_ENTER;
import static com.codeaffine.workflow.test.util.FlowEventLogEntry.EventType.ON_NODE_LEAVE;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.NAME;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.OPERATION_ID;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.OPERATION_ID_1;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.OPERATION_ID_2;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.START;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.codeaffine.workflow.definition.ActivityAspect;
import com.codeaffine.workflow.definition.Task;
import com.codeaffine.workflow.test.util.FlowEventLog;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestActivity;

public class WorkflowImplTest {

  private WorkflowDefinitionHelper definition;
  private FlowEventNotifier notifier;
  private WorkflowImpl workflow;
  private TaskListImpl taskList;

  @Before
  public void setUp() {
    definition = new WorkflowDefinitionHelper();
    notifier = new FlowEventNotifier();
    taskList = new TaskListImpl( new TaskEventNotifier() );
    workflow = new WorkflowImpl( definition.getDefinition(), taskList, notifier, new NodeLoaderImpl() );
  }

  @Test
  public void executeWithSingleOperation() {
    definition.addStartNodeWithOperation( null );
    FlowEventLog log = registerFlowEventLog( notifier );

    workflow.start();

    assertThat( log.getContent() ).isEqualTo( expectedLog(
      $( ON_NODE_ENTER, START ),
      $( ON_NODE_LEAVE, START )
    ) );
  }

  @Test
  public void executeWithSubSequentOperations() {
    definition.addStartNodeWithOperation( OPERATION_ID_1 );
    definition.addActivityNode( OPERATION_ID_1, TestActivity.class, null );
    FlowEventLog log = registerFlowEventLog( notifier );

    workflow.start();

    assertThat( log.getContent() ).isEqualTo( expectedLog(
      $( ON_NODE_ENTER, START ),
      $( ON_NODE_LEAVE, START ),
      $( ON_NODE_ENTER, OPERATION_ID_1 ),
      $( ON_NODE_LEAVE, OPERATION_ID_1 )
    ) );
  }

  @Test
  public void executeWithDecision() {
    definition.addStartNodeWithDecision();
    definition.addActivityNode( OPERATION_ID, TestActivity.class, null );
    definition.addActivityNode( OPERATION_ID_1, TestActivity.class, null );
    definition.addActivityNode( OPERATION_ID_2, TestActivity.class, null );
    FlowEventLog log = registerFlowEventLog( notifier );

    workflow.start();

    assertThat( log.getContent() ).isEqualTo( expectedLog(
      $( ON_NODE_ENTER, START ),
      $( ON_NODE_LEAVE, START ),
      $( ON_NODE_ENTER, OPERATION_ID ),
      $( ON_NODE_LEAVE, OPERATION_ID )
    ) );
  }

  @Test
  public void executeWithTask() {
    definition.addStartNodeWithTask( null );
    FlowEventLog log = registerFlowEventLog( notifier );

    workflow.start();

    assertThat( taskList ).hasSize( 1 );
    assertThat( log.getContent() ).isEqualTo( expectedLog( $( ON_NODE_ENTER, START ) ) );
  }

  @Test
  public void continueWithTask() {
    definition.addStartNodeWithTask( null );
    workflow.start();
    FlowEventLog log = registerFlowEventLog( notifier );

    Task task = taskList.snapshot().get( 0 );
    UUID assigmentId = taskList.acquireAssignment( task );
    taskList.complete( assigmentId );

    assertThat( taskList ).isEmpty();
    assertThat( log.getContent() ).isEqualTo( expectedLog( $( ON_NODE_LEAVE, START ) ) );
  }

  @Test
  public void matchesWithMatcher() {
    definition.registerMatcherFor( VALUE );

    boolean matches = workflow.matches( VALUE );

    assertThat( matches ).isTrue();
  }

  @Test
  public void matchesWithoutMatcher() {
    boolean matches = workflow.matches( null );

    assertThat( matches ).isFalse();
  }

  @Test
  public void defineVariable() {
    Object oldValue = workflow.defineVariable( NAME, VALUE );

    assertThat( oldValue ).isNull();
    assertThat( workflow.getContext().getVariableValue( NAME ) ).isSameAs( VALUE );
  }

  @Test
  public void defineVariableThatAlreadyExists() {
    workflow.defineVariable( NAME, VALUE );

    Object actual = workflow.defineVariable( NAME, new Object() );

    assertThat( actual ).isSameAs( VALUE );
  }

  @Test
  public void copyContext() {
    WorkflowContextImpl toCopy = new WorkflowContextImpl();
    toCopy.defineVariable( NAME, VALUE );

    workflow.copyContext( toCopy );

    assertThat( workflow.getContext().getVariableValue( NAME ) ).isSameAs( VALUE );
  }

  @Test
  public void addActivityAspect() {
    definition.addStartNodeWithOperation( null );
    ActivityAspect aspect = mock( ActivityAspect.class );
    workflow.addActivityAspect( aspect );

    workflow.start();

    InOrder order = inOrder( aspect );
    order.verify( aspect ).beforeExecute( any( TestActivity.class ) );
    order.verify( aspect ).afterExecute( any( TestActivity.class ), eq( ( RuntimeException )null ) );
    order.verifyNoMoreInteractions();
  }
}