package com.codeaffine.eclipse.swt.widget.scrollable;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

public class LayoutContextAssert extends AbstractAssert<LayoutContextAssert, LayoutContext<?>> {

  public LayoutContextAssert( LayoutContext<?> actual ) {
    super( actual, LayoutContextAssert.class );
  }

  public static LayoutContextAssert assertThat( LayoutContext<?> actual ) {
    return new LayoutContextAssert( actual );
  }

  public LayoutContextAssert verticalBarIsVisible() {
    isNotNull();
    if( !actual.isVerticalBarVisible() ) {
      failWithMessage( "Expected vertical scroll bar to be visible but was invisible." );
    }
    return this;
  }

  public LayoutContextAssert verticalBarIsInvisible() {
    isNotNull();
    if( actual.isVerticalBarVisible() ) {
      failWithMessage( "Expected vertical scroll bar to be invisible but was visible." );
    }
    return this;
  }

  public LayoutContextAssert horizontalBarIsVisible() {
    isNotNull();
    if( !actual.isHorizontalBarVisible() ) {
      failWithMessage( "Expected horizontal scroll bar to be visible but was invisible." );
    }
    return this;
  }

  public LayoutContextAssert horizontalBarIsInvisible() {
    isNotNull();
    if( actual.isHorizontalBarVisible() ) {
      failWithMessage( "Expected horizontal scroll bar to be invisible but was visible." );
    }
    return this;
  }

  public LayoutContextAssert hasPreferredSize( Point expected ) {
    isNotNull();
    if( !actual.getPreferredSize().equals( expected ) ) {
      failWithMessage( "Expected preferred tree size to be <%s> but was <%s>.", expected, actual.getPreferredSize() );
    }
    return this;
  }

  public LayoutContextAssert hasNotPreferredSize( Point expected ) {
    isNotNull();
    if( actual.getPreferredSize().equals( expected ) ) {
      failWithMessage( "Expected preferred tree size to be different from <%s>.", expected );
    }
    return this;
  }

  public LayoutContextAssert hasVisibleArea( int x, int y, int width, int height ) {
    return hasVisibleArea( new Rectangle( x, y, width, height ) );
  }

  public LayoutContextAssert hasVisibleArea( Rectangle expected ) {
    isNotNull();
    if( !actual.getVisibleArea().equals( expected ) ) {
      failWithMessage( "Expected visible area to be <%s> but was <%s>.", expected, actual.getVisibleArea() );
    }
    return this;
  }

  public LayoutContextAssert hasVerticalBarOffset( int expected ) {
    isNotNull();
    int actualVerticalBarOffset = actual.getVerticalBarOffset();
    if( actualVerticalBarOffset != expected ) {
      failWithMessage( "Expected vertical bar offset to be <%s> but was <%s>.", expected, actualVerticalBarOffset );
    }
    return this;
  }

  public LayoutContextAssert hasOffset( int expected ) {
    isNotNull();
    if( actual.getOffset() != expected ) {
      failWithMessage( "Expected offset to be <%s> but was <%s>.", expected, actual.getOffset() );
    }
    return this;
  }

  public LayoutContextAssert hasHorizontalAdapterSelection( int expected ) {
    isNotNull();
    int selection = actual.getHorizontalAdapterSelection();
    if( selection != expected ) {
      failWithMessage( "Expected horizontal adapter selection to be <%s> but was <%s>.", expected, selection );
    }
    return this;
  }

  public LayoutContextAssert hasOriginOfScrollableOrdinates( Point expected ) {
    isNotNull();
    Point ordinates = actual.getOriginOfScrollableOrdinates();
    if( !ordinates.equals( expected ) ) {
      failWithMessage( "Expected expected origin scrollable ordinates to be <%s> but were <%s>.", expected, ordinates );
    }
    return this;
  }

  public LayoutContextAssert hasBorderWidth( int expected ) {
    isNotNull();
    if( actual.getBorderWidth() != expected ) {
      failWithMessage( "Expected borderWidth to be <%s> but was <%s>.", expected, actual.getBorderWidth() );
    }
    return this;
  }

  public LayoutContextAssert hasAdapter( Composite expected ) {
    isNotNull();
    if( actual.getAdapter() != expected ) {
      failWithMessage( "Expected adapter to be <%s> but was <%s>.", expected, actual.getAdapter() );
    }
    return this;
  }

  public LayoutContextAssert hasScrollable( Scrollable expected ) {
    isNotNull();
    if( actual.getScrollable() != expected ) {
      failWithMessage( "Expected scrollable to be <%s> but was <%s>.", expected, actual.getAdapter() );
    }
    return this;
  }

  public LayoutContextAssert isScrollableReplacement() {
    isNotNull();
    if( !actual.isScrollableReplacedByAdapter() ) {
      failWithMessage( "Expected adapter to be scrollable replacement." );
    }
    return this;
  }

  public LayoutContextAssert isNotScrollableReplacement() {
    isNotNull();
    if( actual.isScrollableReplacedByAdapter() ) {
      failWithMessage( "Expected adaper not to be scrollable replacement." );
    }
    return this;
  }
}