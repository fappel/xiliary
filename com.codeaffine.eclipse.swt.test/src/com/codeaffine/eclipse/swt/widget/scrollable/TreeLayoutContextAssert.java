package com.codeaffine.eclipse.swt.widget.scrollable;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class TreeLayoutContextAssert extends AbstractAssert<TreeLayoutContextAssert, TreeLayoutContext> {

  public TreeLayoutContextAssert( TreeLayoutContext actual ) {
    super( actual, TreeLayoutContextAssert.class );
  }

  public static TreeLayoutContextAssert assertThat( TreeLayoutContext actual ) {
    return new TreeLayoutContextAssert( actual );
  }

  public TreeLayoutContextAssert verticalBarIsVisible() {
    isNotNull();
    if( !actual.isVerticalBarVisible() ) {
      failWithMessage( "Expected vertical scroll bar to be visible but was invisible."  );
    }
    return this;
  }

  public TreeLayoutContextAssert verticalBarIsInvisible() {
    isNotNull();
    if( actual.isVerticalBarVisible() ) {
      failWithMessage( "Expected vertical scroll bar to be invisible but was visible."  );
    }
    return this;
  }

  public TreeLayoutContextAssert horizontalBarIsVisible() {
    isNotNull();
    if( !actual.isHorizontalBarVisible() ) {
      failWithMessage( "Expected horizontal scroll bar to be visible but was invisible."  );
    }
    return this;
  }

  public TreeLayoutContextAssert horizontalBarIsInvisible() {
    isNotNull();
    if( actual.isHorizontalBarVisible() ) {
      failWithMessage( "Expected horizontal scroll bar to be invisible but was visible."  );
    }
    return this;
  }

  public TreeLayoutContextAssert hasPreferredSize( Point expected ) {
    isNotNull();
    if( !actual.getPreferredSize().equals( expected ) ) {
      failWithMessage( "Expected preferred tree size to be <%s> but was <%s>.", expected, actual.getPreferredSize()  );
    }
    return this;
  }

  public TreeLayoutContextAssert hasVisibleArea( Rectangle expected ) {
    isNotNull();
    if( !actual.getVisibleArea().equals( expected ) ) {
      failWithMessage( "Expected visible area to be <%s> but was <%s>.", expected, actual.getVisibleArea() );
    }
    return this;
  }

  public TreeLayoutContextAssert hasVerticalBarOffset( int expected ) {
    isNotNull();
    int actualVerticalBarOffset = actual.getVerticalBarOffset();
    if( actualVerticalBarOffset != expected ) {
      failWithMessage( "Expected vertical bar offset to be <%s> but was <%s>.", expected, actualVerticalBarOffset );
    }
    return this;
  }
}