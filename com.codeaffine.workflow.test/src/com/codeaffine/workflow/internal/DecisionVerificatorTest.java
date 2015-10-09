package com.codeaffine.workflow.internal;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static com.codeaffine.workflow.internal.DecisionVerificator.ERROR_UNREACHABLE_NODE;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.DECISION_ID;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.OPERATION_ID;
import static com.codeaffine.workflow.test.util.WorkflowDefinitionHelper.OPERATION_ID_1;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.workflow.NodeDefinition;
import com.codeaffine.workflow.definition.Decision;

public class DecisionVerificatorTest {

  private DecisionVerificator verificator;
  private NodeDefinition nodeDefinition;

  @Before
  public void setUp() {
    verificator = new DecisionVerificator();
    nodeDefinition = new NodeDefinition( DECISION_ID, Decision.class, OPERATION_ID );
  }

  @Test
  public void verify() {
    Throwable actual = thrownBy( () -> verificator.verify( nodeDefinition, OPERATION_ID ) );

    assertThat( actual ).isNull();
  }

  @Test
  public void verifyWithUnreachableSuccessor() {
    Throwable actual = thrownBy( () -> verificator.verify( nodeDefinition, OPERATION_ID_1 ) );

    assertThat( actual )
      .isInstanceOf( IllegalStateException.class )
      .hasMessage( String.format( ERROR_UNREACHABLE_NODE, OPERATION_ID_1, DECISION_ID ) );
  }
}