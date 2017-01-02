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

import static java.lang.Integer.valueOf;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class RectangleAssert extends AbstractAssert<RectangleAssert, Rectangle> {

  static final String X_PATTERN = "Expected actual x value to be <%s> but it was <%s>.";
  static final String Y_PATTERN = "Expected actual y value to be <%s> but it was <%s>.";
  static final String WIDTH_PATTERN = "Expected actual width value to be <%s> but it was <%s>.";
  static final String HEIGHT_PATTERN = "Expected actual height value to be <%s> but it was <%s>.";
  static final String CONTAINS_PATTERN = "Expected actual to contain <%s> but it does not.";
  static final String INTERSECTS_PATTERN = "Expected actual to intersect with <%s> but it does not.";
  static final String EMPTY_PATTERN = "Expected actual to be empty but it was not.";

  public RectangleAssert( Rectangle actual ) {
    super( actual, RectangleAssert.class );
  }

  public static RectangleAssert assertThat( Rectangle actual ) {
    return new RectangleAssert( actual );
  }

  public RectangleAssert isEqualToRectangleOf( int x, int y, int width, int height ) {
    return isEqualTo( new Rectangle( x, y, width, height ) );
  }

  public RectangleAssert hasX( int expected ) {
    isNotNull();
    checkValue( actual.x, expected, X_PATTERN );
    return this;
  }

  public RectangleAssert hasY( int expected ) {
    isNotNull();
    checkValue( actual.y, expected, Y_PATTERN );
    return this;
  }

  public RectangleAssert hasWidth( int expected ) {
    isNotNull();
    checkValue( actual.width, expected, WIDTH_PATTERN );
    return this;
  }

  public RectangleAssert hasHeight( int expected ) {
    isNotNull();
    checkValue( actual.height, expected, HEIGHT_PATTERN );
    return this;
  }

  public RectangleAssert containsPointOf( int expectedX, int expectedY ) {
    return contains( new Point( expectedX, expectedY ) );
  }

  public RectangleAssert contains( Point expected ) {
    isNotNull();
    if( !actual.contains( expected ) ) {
      failWithMessage( CONTAINS_PATTERN, expected );
    }
    return this;
  }

  public RectangleAssert intersects( Rectangle expected ) {
    return intersectsWithRectangle( expected.x, expected.y, expected.width, expected.height );
  }

  public RectangleAssert intersectsWithRectangle( int x, int y, int width, int height ) {
    isNotNull();
    if( !actual.intersects( x, y, width, height ) ) {
      failWithMessage( INTERSECTS_PATTERN, new Rectangle( x, y, width, height ) );
    }
    return this;
  }

  public RectangleAssert isEmpty() {
    isNotNull();
    if( !actual.isEmpty() ) {
      failWithMessage( EMPTY_PATTERN );
    }
    return this;
  }

  private void checkValue( int actualValue, int expectedValue, String pattern ) {
    if( actualValue != expectedValue ) {
      failWithMessage( pattern, valueOf( expectedValue ), valueOf( actualValue ) );
    }
  }
}