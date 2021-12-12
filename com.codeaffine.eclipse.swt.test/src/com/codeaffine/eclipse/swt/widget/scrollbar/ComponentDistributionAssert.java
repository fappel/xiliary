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
package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.assertj.core.api.AbstractAssert;

public class ComponentDistributionAssert extends AbstractAssert<ComponentDistributionAssert, ComponentDistribution> {

  private static final String FAILURE_PATTERN = "Expected %s to be <%s> but was <%s>.";

  public ComponentDistributionAssert( ComponentDistribution actual ) {
    super( actual, ComponentDistributionAssert.class );
  }

  public static ComponentDistributionAssert assertThat( ComponentDistribution actual ) {
    return new ComponentDistributionAssert( actual );
  }

  public ComponentDistributionAssert hasUpFastLength( int expected) {
    isNotNull();
    hasExpectedFieldValue( "upFastLength", expected, actual.upFastLength );
    return this;
  }

  public ComponentDistributionAssert hasDragStart( int expected ) {
    isNotNull();
    hasExpectedFieldValue( "dragStart", expected, actual.dragStart );
    return this;
  }

  public ComponentDistributionAssert hasDragLength( int expected ) {
    isNotNull();
    hasExpectedFieldValue( "dragLength", expected, actual.dragLength );
    return this;
  }

  public ComponentDistributionAssert hasDownFastStart( int expected ) {
    isNotNull();
    hasExpectedFieldValue( "downFastStart", expected, actual.downFastStart );
    return this;
  }

  public ComponentDistributionAssert hasDownFastLength( int expected ) {
    isNotNull();
    hasExpectedFieldValue( "downFastLength", expected, actual.downFastLength );
    return this;
  }

  public ComponentDistributionAssert hasDownStart( int expected ) {
    isNotNull();
    hasExpectedFieldValue( "downStart", expected, actual.downStart );
    return this;
  }

  private void hasExpectedFieldValue( String fieldName, int expectedValue, int actualValue ) {
    if( actualValue != expectedValue ) {
      failWithMessage( FAILURE_PATTERN, fieldName, expectedValue, actualValue );
    }
  }
}