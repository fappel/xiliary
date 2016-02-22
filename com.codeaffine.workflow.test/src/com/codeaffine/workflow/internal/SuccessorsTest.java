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

import static com.codeaffine.workflow.internal.Successors.EMYPTY_SUCCESSORS;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SuccessorsTest {

  @Test
  public void toSuccessors() {
    String expected = "successor";

    String[] actual = Successors.toSuccessors( expected );

    assertThat( actual )
      .hasSize( 1 )
      .contains( expected );
  }

  @Test
  public void toSuccessorsWithNullParameter() {
    String[] actual = Successors.toSuccessors( null );

    assertThat( actual ).isSameAs( EMYPTY_SUCCESSORS );
  }

  @Test
  public void concat() {
    String[] expected = new String[] { "successor1", "successor2", "successor3" };

    String[] actual = Successors.concat( expected[ 0 ], expected[ 1 ], expected[ 2 ] );

    assertThat( actual ).isEqualTo( expected );
  }
}