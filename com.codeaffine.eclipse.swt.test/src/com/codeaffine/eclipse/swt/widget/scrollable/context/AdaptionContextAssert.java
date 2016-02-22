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
package com.codeaffine.eclipse.swt.widget.scrollable.context;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

public class AdaptionContextAssert extends AbstractAssert<AdaptionContextAssert, AdaptionContext<?>> {

  public AdaptionContextAssert( AdaptionContext<?> actual ) {
    super( actual, AdaptionContextAssert.class );
  }

  public static AdaptionContextAssert assertThat( AdaptionContext<?> actual ) {
    return new AdaptionContextAssert( actual );
  }

  public AdaptionContextAssert verticalBarIsVisible() {
    isNotNull();
    if( !actual.isVerticalBarVisible() ) {
      failWithMessage( "Expected vertical scroll bar to be visible but was invisible." );
    }
    return this;
  }

  public AdaptionContextAssert verticalBarIsInvisible() {
    isNotNull();
    if( actual.isVerticalBarVisible() ) {
      failWithMessage( "Expected vertical scroll bar to be invisible but was visible." );
    }
    return this;
  }

  public AdaptionContextAssert horizontalBarIsVisible() {
    isNotNull();
    if( !actual.isHorizontalBarVisible() ) {
      failWithMessage( "Expected horizontal scroll bar to be visible but was invisible." );
    }
    return this;
  }

  public AdaptionContextAssert horizontalBarIsInvisible() {
    isNotNull();
    if( actual.isHorizontalBarVisible() ) {
      failWithMessage( "Expected horizontal scroll bar to be invisible but was visible." );
    }
    return this;
  }

  public AdaptionContextAssert hasPreferredSize( Point expected ) {
    isNotNull();
    if( !actual.getPreferredSize().equals( expected ) ) {
      failWithMessage( "Expected preferred tree size to be <%s> but was <%s>.", expected, actual.getPreferredSize() );
    }
    return this;
  }

  public AdaptionContextAssert hasNotPreferredSize( Point expected ) {
    isNotNull();
    if( actual.getPreferredSize().equals( expected ) ) {
      failWithMessage( "Expected preferred tree size to be different from <%s>.", expected );
    }
    return this;
  }

  public AdaptionContextAssert hasVisibleArea( int x, int y, int width, int height ) {
    return hasVisibleArea( new Rectangle( x, y, width, height ) );
  }

  public AdaptionContextAssert hasVisibleArea( Rectangle expected ) {
    isNotNull();
    if( !actual.getVisibleArea().equals( expected ) ) {
      failWithMessage( "Expected visible area to be <%s> but was <%s>.", expected, actual.getVisibleArea() );
    }
    return this;
  }

  public AdaptionContextAssert hasVerticalBarOffset( int expected ) {
    isNotNull();
    int actualVerticalBarOffset = actual.getVerticalBarOffset();
    if( actualVerticalBarOffset != expected ) {
      failWithMessage( "Expected vertical bar offset to be <%s> but was <%s>.", expected, actualVerticalBarOffset );
    }
    return this;
  }

  public AdaptionContextAssert hasOffset( int expected ) {
    isNotNull();
    if( actual.getOffset() != expected ) {
      failWithMessage( "Expected offset to be <%s> but was <%s>.", expected, actual.getOffset() );
    }
    return this;
  }

  public AdaptionContextAssert hasHorizontalAdapterSelection( int expected ) {
    isNotNull();
    int selection = actual.getHorizontalAdapterSelection();
    if( selection != expected ) {
      failWithMessage( "Expected horizontal adapter selection to be <%s> but was <%s>.", expected, selection );
    }
    return this;
  }

  public AdaptionContextAssert hasOriginOfScrollableOrdinates( Point expected ) {
    isNotNull();
    Point ordinates = actual.getOriginOfScrollableOrdinates();
    if( !ordinates.equals( expected ) ) {
      failWithMessage( "Expected expected origin scrollable ordinates to be <%s> but were <%s>.", expected, ordinates );
    }
    return this;
  }

  public AdaptionContextAssert hasBorderWidth( int expected ) {
    isNotNull();
    if( actual.getBorderWidth() != expected ) {
      failWithMessage( "Expected borderWidth to be <%s> but was <%s>.", expected, actual.getBorderWidth() );
    }
    return this;
  }

  public AdaptionContextAssert hasAdapter( Composite expected ) {
    isNotNull();
    if( actual.getAdapter() != expected ) {
      failWithMessage( "Expected adapter to be <%s> but was <%s>.", expected, actual.getAdapter() );
    }
    return this;
  }

  public AdaptionContextAssert hasScrollable( Scrollable expected ) {
    isNotNull();
    if( !actual.getScrollable().isSameAs( expected ) ) {
      failWithMessage( "Expected scrollable to be <%s> but was <%s>.", expected, actual.getAdapter() );
    }
    return this;
  }

  public AdaptionContextAssert isScrollableReplacement() {
    isNotNull();
    if( !actual.isScrollableReplacedByAdapter() ) {
      failWithMessage( "Expected adapter to be scrollable replacement." );
    }
    return this;
  }

  public AdaptionContextAssert isNotScrollableReplacement() {
    isNotNull();
    if( actual.isScrollableReplacedByAdapter() ) {
      failWithMessage( "Expected adaper not to be scrollable replacement." );
    }
    return this;
  }
}