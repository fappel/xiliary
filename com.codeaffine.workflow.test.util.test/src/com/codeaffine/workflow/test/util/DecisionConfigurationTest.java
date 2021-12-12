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
package com.codeaffine.workflow.test.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class DecisionConfigurationTest {

  private static final String DECISION1 = "decision1";
  private static final String DECISION = "decision";
  private static final String NODE = "node";

  @Test
  public void constructor() {
    DecisionConfiguration configuration = new DecisionConfiguration( NODE, DECISION, DECISION1 );

    assertThat( configuration.nodeId ).isSameAs( NODE );
    assertThat( configuration.decision).isSameAs( DECISION );
    assertThat( configuration.decisions ).containsExactly( DECISION1 );
  }
}