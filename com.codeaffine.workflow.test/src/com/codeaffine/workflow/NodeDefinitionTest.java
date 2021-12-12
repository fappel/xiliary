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
package com.codeaffine.workflow;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class NodeDefinitionTest {

  private static final String ID = "id";
  private static final Class<Runnable> TYPE = Runnable.class;
  private static final String[] SUCCESSORS = new String[] { "successor" };

  private NodeDefinition nodeDefinition;

  @Before
  public void setUp() {
    nodeDefinition = new NodeDefinition( ID, TYPE, SUCCESSORS );
  }

  @Test
  public void getNodeId() {
    String actual = nodeDefinition.getNodeId();

    assertThat( actual ).isSameAs( ID );
  }

  @Test
  public void getType() {
    Class<?> actual = nodeDefinition.getType();

    assertThat( ( Object )actual ).isSameAs( TYPE );
  }

  @Test
  public void getSuccessors() {
    String[] actual = nodeDefinition.getSuccessors();

    assertThat( actual )
      .isNotSameAs( SUCCESSORS )
      .isEqualTo( SUCCESSORS );
  }
}
