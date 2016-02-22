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
package com.codeaffine.eclipse.swt.layout;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.swt.layout.FillLayout;

public class FillLayoutAssert extends AbstractAssert<FillLayoutAssert, FillLayout> {

  private static final String FAILURE_PATTERN = "Expected %s to be <%s> but was <%s>.";

  public FillLayoutAssert( FillLayout actual ) {
    super( actual, FillLayoutAssert.class );
  }

  public static FillLayoutAssert assertThat( FillLayout actual ) {
    return new FillLayoutAssert( actual );
  }

  public FillLayoutAssert hasType( int expected ) {
    isNotNull();
    isEquals( "type", expected, actual.type );
    return this;
  }

  public FillLayoutAssert hasSpacing( int expected ) {
    isNotNull();
    isEquals( "spacing", expected, actual.spacing );
    return this;
  }

  public FillLayoutAssert hasMarginWidth( int expected ) {
    isNotNull();
    isEquals( "marginWidth", expected, actual.marginWidth );
    return this;
  }

  public FillLayoutAssert hasMarginHeight( int expected ) {
    isNotNull();
    isEquals( "marginHeight", expected, actual.marginHeight );
    return this;
  }

  public FillLayoutAssert hasMargin( int expected ) {
    hasMarginWidth( expected );
    hasMarginHeight( expected );
    return this;
  }

  @Override
  public FillLayoutAssert isEqualTo( Object other ) {
    isNotNull();
    isExactlyInstanceOf( FillLayout.class );
    FillLayout otherFillLayout = ( FillLayout )other;
    hasMarginWidth( otherFillLayout.marginWidth );
    hasMarginHeight( otherFillLayout.marginHeight );
    hasType( otherFillLayout.type );
    hasSpacing( otherFillLayout.spacing );
    return this;
  }

  @Override
  public FillLayoutAssert isNotEqualTo( Object other ) {
    super.isNotEqualTo( other );
    FillLayout otherFillLayout = ( FillLayout )other;
    if(    actual.marginWidth == ( otherFillLayout.marginWidth )
        && actual.marginHeight == ( otherFillLayout.marginHeight )
        && actual.type == ( otherFillLayout.type )
        && actual.spacing == ( otherFillLayout.spacing ) )
    {
      failWithMessage( "Expect other FillLayout instance not to be equal but was." );
    }
    return this;
  }

  private void isEquals( String name, int first, int second ) {
    if( first != second ) {
      failWithMessage( FAILURE_PATTERN, name, first, second );
    }
  }
}