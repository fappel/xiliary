package com.codeaffine.workflow;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class WorkflowContextEventTest {

  private static final VariableDeclaration<String> VARIABLE_DECLARATION
    = new VariableDeclaration<String>( "variable", String.class  );
  private static final String NEW_VALUE = "newValue";
  private static final String OLD_VALUE = "oldValue";

  @Test
  public void testConstructorAssignments() {
    WorkflowContextEvent<String> event = new WorkflowContextEvent<String>( VARIABLE_DECLARATION, NEW_VALUE, OLD_VALUE );

    assertThat( event.getDeclaration() ).isSameAs( VARIABLE_DECLARATION );
    assertThat( event.getNewValue() ).isSameAs( NEW_VALUE );
    assertThat( event.getOldValue() ).isSameAs( OLD_VALUE );
  }
}