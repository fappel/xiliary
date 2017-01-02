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

import static com.codeaffine.eclipse.swt.test.util.graphics.RectangleAssert.CONTAINS_PATTERN;
import static com.codeaffine.eclipse.swt.test.util.graphics.RectangleAssert.EMPTY_PATTERN;
import static com.codeaffine.eclipse.swt.test.util.graphics.RectangleAssert.HEIGHT_PATTERN;
import static com.codeaffine.eclipse.swt.test.util.graphics.RectangleAssert.INTERSECTS_PATTERN;
import static com.codeaffine.eclipse.swt.test.util.graphics.RectangleAssert.WIDTH_PATTERN;
import static com.codeaffine.eclipse.swt.test.util.graphics.RectangleAssert.X_PATTERN;
import static com.codeaffine.eclipse.swt.test.util.graphics.RectangleAssert.Y_PATTERN;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Consumer;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.junit.Test;

public class RectangleAssertTest {

  private static final String MUST_NOT_BE_NULL = "Expecting actual not to be null";

  @Test
  public void isEqualToRectangleOf() {
    RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).isEqualToRectangleOf( 1, 2, 3, 4 );
  }

  @Test
  public void hasX() {
    RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).hasX( 1 );
  }

  @Test
  public void hasXWithNullAsActualRectangle() {
    verifyNullCheck( rectangleAssertOnNull -> rectangleAssertOnNull.hasX( 1 ) );
  }

  @Test
  public void hasXFailure() {
    Throwable actual = thrownBy( () -> RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).hasX( 2 ) );

    assertThat( actual )
      .isInstanceOf( AssertionError.class )
      .hasMessage( format( X_PATTERN, 2, 1 ) );
  }

  @Test
  public void hasY() {
    RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).hasY( 2 );
  }

  @Test
  public void hasYWithNullAsActualRectangle() {
    verifyNullCheck( rectangleAssertOnNull -> rectangleAssertOnNull.hasY( 1 ) );
  }

  @Test
  public void hasYFailure() {
    Throwable actual = thrownBy( () -> RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).hasY( 1 ) );

    assertThat( actual )
      .isInstanceOf( AssertionError.class )
      .hasMessage( format( Y_PATTERN, 1, 2 ) );
  }

  @Test
  public void hasWidth() {
    RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).hasWidth( 3 );
  }

  @Test
  public void hasWidthWithNullAsActualRectangle() {
    verifyNullCheck( rectangleAssertOnNull -> rectangleAssertOnNull.hasWidth( 1 ) );
  }

  @Test
  public void hasWidthFailure() {
    Throwable actual = thrownBy( () -> RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).hasWidth( 1 ) );

    assertThat( actual )
      .isInstanceOf( AssertionError.class )
      .hasMessage( format( WIDTH_PATTERN, 1, 3 ) );
  }

  @Test
  public void hasHeight() {
    RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).hasHeight( 4 );
  }

  @Test
  public void hasHeightWithNullAsActualRectangle() {
    verifyNullCheck( rectangleAssertOnNull -> rectangleAssertOnNull.hasHeight( 1 ) );
  }

  @Test
  public void hasHeightFailure() {
    Throwable actual = thrownBy( () -> RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).hasHeight( 1 ) );

    assertThat( actual )
      .isInstanceOf( AssertionError.class )
      .hasMessage( format( HEIGHT_PATTERN, 1, 4 ) );
  }

  @Test
  public void containsPointOf() {
    RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).containsPointOf( 1, 2 );
  }

  @Test
  public void containsPoint() {
    RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).contains( new Point( 1, 2 ) );
  }

  @Test
  public void containsPointWithNullAsActualRectangle() {
    verifyNullCheck( rectangleAssertOnNull -> rectangleAssertOnNull.contains( new Point( 1, 2 ) ) );
  }

  @Test
  public void containsFailure() {
    Throwable actual = thrownBy( () -> {
      RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).contains( new Point( 5, 6 ) );
    } );

    assertThat( actual )
      .isInstanceOf( AssertionError.class )
      .hasMessage( format( CONTAINS_PATTERN, new Point( 5, 6 ) ) );
  }

  @Test
  public void intersectsWithRectangle() {
    RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).intersectsWithRectangle( 1, 2, 3, 4 );
  }

  @Test
  public void intersectsWithRectangleWithNullAsActualRectangle() {
    verifyNullCheck( rectangleAssertOnNull -> rectangleAssertOnNull.intersectsWithRectangle( 1, 2, 3, 4 ) );
  }

  @Test
  public void intersectsWithRectangleFailure() {
    Throwable actual = thrownBy( () -> {
      RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).intersectsWithRectangle( 10, 20, 30, 40 );
    } );

    assertThat( actual )
      .isInstanceOf( AssertionError.class )
      .hasMessage( format( INTERSECTS_PATTERN, new Rectangle( 10, 20, 30, 40 ) ) );
  }

  @Test
  public void intersects() {
    RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).intersects( new Rectangle( 1, 2, 3, 4 ) );
  }

  @Test
  public void isEmpty() {
    RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 0 ) ).isEmpty();
  }

  @Test
  public void isEmptyWithNullAsActualRectangle() {
    verifyNullCheck( rectangleAssertOnNull -> rectangleAssertOnNull.isEmpty() );
  }

  @Test
  public void isEmptyFailure() {
    verifyNullCheck( rectangleAssertOnNull -> rectangleAssertOnNull.isEmpty() );
    Throwable actual = thrownBy( () -> RectangleAssert.assertThat( new Rectangle( 1, 2, 3, 4 ) ).isEmpty() );

    assertThat( actual )
      .isInstanceOf( AssertionError.class )
      .hasMessage( EMPTY_PATTERN );
  }

  private static void verifyNullCheck( Consumer<RectangleAssert> consumer ) {
    Throwable actual = thrownBy( () -> consumer.accept( RectangleAssert.assertThat( null ) ) );

    assertThat( actual )
      .isInstanceOf( AssertionError.class )
      .hasMessageContaining( MUST_NOT_BE_NULL );
  }
}