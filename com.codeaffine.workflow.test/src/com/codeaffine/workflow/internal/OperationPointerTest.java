package com.codeaffine.workflow.internal;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrown;
import static com.codeaffine.workflow.internal.DecisionVerificator.ERROR_UNREACHABLE_NODE;
import static com.codeaffine.workflow.internal.OperationPointer.ERROR_ILLEGAL_MOVE;
import static com.codeaffine.workflow.internal.OperationPointer.ERROR_UNAVAILABLE_OPERATION;
import static com.codeaffine.workflow.test.util.FlowEventLog.$;
import static com.codeaffine.workflow.test.util.FlowEventLog.expectedLog;
import static com.codeaffine.workflow.test.util.FlowEventLogEntry.EventType.ON_NODE_ENTER;
import static com.codeaffine.workflow.test.util.FlowEventLogEntry.EventType.ON_NODE_LEAVE;
import static com.codeaffine.workflow.test.util.NodeDefinitionAssert.assertThat;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.DECISION_ID;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.OPERATION_ID;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.OPERATION_ID_1;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.OPERATION_ID_2;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.test.util.lang.ThrowableCaptor.Actor;
import com.codeaffine.workflow.NodeDefinition;
import com.codeaffine.workflow.persistence.OperationPointerMemento;
import com.codeaffine.workflow.test.util.FlowEventLog;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestActivity;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestDecision;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestTask;

public class OperationPointerTest {

  private WorkflowDefinitionImpl definition;
  private WorkflowContextImpl context;
  private FlowEventNotifier notifier;
  private OperationPointer pointer;

  @Before
  public void setUp() {
    definition = new WorkflowDefinitionImpl();
    notifier = new FlowEventNotifier();
    context = new WorkflowContextImpl();
    pointer = createOperationPointer();
  }

  @Test
  public void move() {
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( OPERATION_ID );

    pointer.move();
    NodeDefinition actual = pointer.acquire();

    assertThat( actual )
      .hasNodeId( OPERATION_ID )
      .hasType( TestActivity.class )
      .hasSuccessors();
  }

  @Test
  public void moveWithDecision() {
    definition.addDecision( DECISION_ID, TestDecision.class, OPERATION_ID, OPERATION_ID_1 );
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( DECISION_ID );

    pointer.move();
    NodeDefinition actual = pointer.acquire();

    assertThat( actual )
      .hasNodeId( OPERATION_ID )
      .hasType( TestActivity.class )
      .hasSuccessors();
  }

  @Test
  public void moveWithDecisionToUnreachableNode() {
    definition.addDecision( DECISION_ID, TestDecision.class, OPERATION_ID_1, OPERATION_ID_2 );
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( DECISION_ID );

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        pointer.move();
      }
    } );

    assertThat( actual )
      .hasMessage( format( ERROR_UNREACHABLE_NODE, OPERATION_ID, DECISION_ID ) )
      .isInstanceOf( IllegalStateException.class );
  }

  @Test
  public void moveRepeatedly() {
    definition.addActivity( OPERATION_ID_1, TestActivity.class, DECISION_ID );
    definition.addDecision( DECISION_ID, TestDecision.class, OPERATION_ID, OPERATION_ID_1 );
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( OPERATION_ID_1 );

    pointer.move();
    pointer.acquire();
    pointer.move();
    NodeDefinition actual = pointer.acquire();

    assertThat( actual )
      .hasNodeId( OPERATION_ID )
      .hasType( TestActivity.class )
      .hasSuccessors();
  }

  @Test
  public void moveOnUnacquiredOperation() {
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( OPERATION_ID );
    pointer.move();

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() {
        pointer.move();
      }
    } );

    assertThat( actual )
      .isInstanceOf( IllegalStateException.class )
      .hasMessage( ERROR_ILLEGAL_MOVE );
  }

  @Test
  public void isAvailable() {
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( OPERATION_ID );

    pointer.move();

    assertThat( pointer.isAvailable() ).isTrue();
  }

  @Test
  public void isAvailableAfterAcquire() {
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( OPERATION_ID );
    pointer.move();

    pointer.acquire();

    assertThat( pointer.isAvailable() ).isFalse();
  }

  @Test
  public void isAvailableBeforeInitialization() {
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( OPERATION_ID );

    assertThat( pointer.isAvailable() ).isFalse();
  }

  @Test
  public void isAvailableAfterMoveToEnd() {
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( OPERATION_ID );

    pointer.move();
    pointer.acquire();
    pointer.move();

    assertThat( pointer.isAvailable() ).isFalse();
  }

  @Test
  public void acquireOnUnavailableOperation() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() {
        pointer.acquire();
      }
    } );

    assertThat( actual )
      .isInstanceOf( IllegalStateException.class )
      .hasMessage( ERROR_UNAVAILABLE_OPERATION );
  }

  @Test
  public void flowNotifications() {
    definition.addTask( OPERATION_ID_1, TestTask.class, DECISION_ID );
    definition.addDecision( DECISION_ID, TestDecision.class, OPERATION_ID, OPERATION_ID_1  );
    definition.addTask( OPERATION_ID, TestTask.class, null );
    definition.setStart( OPERATION_ID_1 );
    FlowEventLog flowEventLog = FlowEventLog.registerFlowEventLog( notifier );

    pointer.move();
    pointer.acquire();
    pointer.move();
    pointer.acquire();
    pointer.move();

    assertThat( flowEventLog.getContent() ).isEqualTo( expectedLog(
      $( ON_NODE_ENTER, OPERATION_ID_1 ),
      $( ON_NODE_LEAVE, OPERATION_ID_1 ),
      $( ON_NODE_ENTER, DECISION_ID ),
      $( ON_NODE_LEAVE, DECISION_ID ),
      $( ON_NODE_ENTER, OPERATION_ID ),
      $( ON_NODE_LEAVE, OPERATION_ID )
    ) );
  }

  @Test
  public void saveAndRestore() {
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( OPERATION_ID );
    pointer.move();

    OperationPointerMemento memento = pointer.save();
    OperationPointer newPointer = createOperationPointer();
    newPointer.restore( memento );
    NodeDefinition actual = newPointer.acquire();

    assertThat( actual )
      .hasNodeId( OPERATION_ID )
      .hasType( TestActivity.class )
      .hasSuccessors();
  }

  private OperationPointer createOperationPointer() {
    return new OperationPointer( new NodeLoaderImpl(), notifier, context, definition );
  }
}