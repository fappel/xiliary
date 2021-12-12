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
package com.codeaffine.eclipse.swt.test.util.graphics;

import static java.lang.Integer.valueOf;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.swt.graphics.Point;

public class PointAssert extends AbstractAssert<PointAssert, Point> {

  static final String X_PATTERN = "Expected actual x value to be <%s> but it was <%s>.";
  static final String Y_PATTERN = "Expected actual y value to be <%s> but it was <%s>.";

  public PointAssert( Point actual ) {
    super( actual, PointAssert.class );
  }

  public static PointAssert assertThat( Point actual ) {
    return new PointAssert( actual );
  }

  public PointAssert isEqualToPointOf( int x, int y ) {
    return isEqualTo( new Point( x, y ) );
  }

  public PointAssert hasX( int expected ) {
    isNotNull();
    checkValue( actual.x, expected, X_PATTERN );
    return this;
  }

  public PointAssert hasY( int expected ) {
    isNotNull();
    checkValue( actual.y, expected, Y_PATTERN );
    return this;
  }

  private void checkValue( int actualValue, int expectedValue, String pattern ) {
    if( actualValue != expectedValue ) {
      failWithMessage( pattern, valueOf( expectedValue ), valueOf( actualValue ) );
    }
  }
}