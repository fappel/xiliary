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
package com.codeaffine.workflow.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.event.WorkflowContextEvent;

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