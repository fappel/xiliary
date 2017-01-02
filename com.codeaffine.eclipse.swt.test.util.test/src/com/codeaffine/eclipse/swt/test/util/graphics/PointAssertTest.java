/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.test.util.graphics;

import static com.codeaffine.eclipse.swt.test.util.graphics.PointAssert.X_PATTERN;
import static com.codeaffine.eclipse.swt.test.util.graphics.PointAssert.Y_PATTERN;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Consumer;

import org.eclipse.swt.graphics.Point;
import org.junit.Test;

public class PointAssertTest {

  private static final String MUST_NOT_BE_NULL = "Expecting actual not to be null";

  @Test
  public void isEqualToPointOf() {
    PointAssert.assertThat( new Point( 1, 2 ) ).isEqualToPointOf( 1, 2 );
  }

  @Test
  public void hasX() {
    PointAssert.assertThat( new Point( 1, 2 ) ).hasX( 1 );
  }

  @Test
  public void hasXWithNullAsActualPoint() {
    verifyNullCheck( pointAssertOnNull -> pointAssertOnNull.hasX( 1 ) );
  }

  @Test
  public void hasXFailure() {
    Throwable actual = thrownBy( () -> PointAssert.assertThat( new Point( 1, 2 ) ).hasX( 2 ) );

    assertThat( actual )
      .isInstanceOf( AssertionError.class )
      .hasMessage( format( X_PATTERN, 2, 1 ) );
  }

  @Test
  public void hasY() {
    PointAssert.assertThat( new Point( 1, 2 ) ).hasY( 2 );
  }

  @Test
  public void hasYWithNullAsActualPoint() {
    verifyNullCheck( pointAssertOnNull -> pointAssertOnNull.hasY( 1 ) );
  }

  @Test
  public void hasYFailure() {
    Throwable actual = thrownBy( () -> PointAssert.assertThat( new Point( 1, 2 ) ).hasY( 1 ) );

    assertThat( actual )
      .isInstanceOf( AssertionError.class )
      .hasMessage( format( Y_PATTERN, 1, 2 ) );
  }

  private static void verifyNullCheck( Consumer<PointAssert> consumer ) {
    Throwable actual = thrownBy( () -> consumer.accept( PointAssert.assertThat( null ) ) );

    assertThat( actual )
      .isInstanceOf( AssertionError.class )
      .hasMessageContaining( MUST_NOT_BE_NULL );
  }
}