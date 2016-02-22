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

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static com.codeaffine.workflow.internal.DecisionVerificator.ERROR_UNREACHABLE_NODE;
import static com.codeaffine.workflow.internal.FlowProcessor.ERROR_ILLEGAL_MOVE;
import static com.codeaffine.workflow.internal.FlowProcessor.ERROR_UNAVAILABLE_OPERATION;
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

import com.codeaffine.workflow.NodeDefinition;
import com.codeaffine.workflow.persistence.FlowProcessorMemento;
import com.codeaffine.workflow.test.util.FlowEventLog;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestActivity;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestDecision;
import com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.TestTask;

public class FlowProcessorTest {

  private WorkflowDefinitionImpl definition;
  private WorkflowContextImpl context;
  private FlowEventNotifier notifier;
  private FlowProcessor flowProcessor;

  @Before
  public void setUp() {
    definition = new WorkflowDefinitionImpl();
    notifier = new FlowEventNotifier();
    context = new WorkflowContextImpl();
    flowProcessor = createFlowProcessor();
  }

  @Test
  public void move() {
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( OPERATION_ID );

    flowProcessor.move();
    NodeDefinition actual = flowProcessor.acquire();

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

    flowProcessor.move();
    NodeDefinition actual = flowProcessor.acquire();

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

    Throwable actual = thrownBy( () -> flowProcessor.move() );

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

    flowProcessor.move();
    flowProcessor.acquire();
    flowProcessor.move();
    NodeDefinition actual = flowProcessor.acquire();

    assertThat( actual )
      .hasNodeId( OPERATION_ID )
      .hasType( TestActivity.class )
      .hasSuccessors();
  }

  @Test
  public void moveOnUnacquiredOperation() {
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( OPERATION_ID );
    flowProcessor.move();

    Throwable actual = thrownBy( () -> flowProcessor.move() );

    assertThat( actual )
      .isInstanceOf( IllegalStateException.class )
      .hasMessage( ERROR_ILLEGAL_MOVE );
  }

  @Test
  public void isAvailable() {
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( OPERATION_ID );

    flowProcessor.move();

    assertThat( flowProcessor.isAvailable() ).isTrue();
  }

  @Test
  public void isAvailableAfterAcquire() {
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( OPERATION_ID );
    flowProcessor.move();

    flowProcessor.acquire();

    assertThat( flowProcessor.isAvailable() ).isFalse();
  }

  @Test
  public void isAvailableBeforeInitialization() {
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( OPERATION_ID );

    assertThat( flowProcessor.isAvailable() ).isFalse();
  }

  @Test
  public void isAvailableAfterMoveToEnd() {
    definition.addActivity( OPERATION_ID, TestActivity.class, null );
    definition.setStart( OPERATION_ID );

    flowProcessor.move();
    flowProcessor.acquire();
    flowProcessor.move();

    assertThat( flowProcessor.isAvailable() ).isFalse();
  }

  @Test
  public void acquireOnUnavailableOperation() {
    Throwable actual = thrownBy( () -> flowProcessor.acquire() );

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

    flowProcessor.move();
    flowProcessor.acquire();
    flowProcessor.move();
    flowProcessor.acquire();
    flowProcessor.move();

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
    flowProcessor.move();

    FlowProcessorMemento memento = flowProcessor.save();
    FlowProcessor newFlowProcessor = createFlowProcessor();
    newFlowProcessor.restore( memento );
    NodeDefinition actual = newFlowProcessor.acquire();

    assertThat( actual )
      .hasNodeId( OPERATION_ID )
      .hasType( TestActivity.class )
      .hasSuccessors();
  }

  private FlowProcessor createFlowProcessor() {
    return new FlowProcessor( new NodeLoaderImpl(), notifier, context, definition );
  }
}