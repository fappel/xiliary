package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;
import static java.lang.Math.max;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Scrollable;

class ScrollableLayouter {

  private final Scrollable scrollable;

  ScrollableLayouter( Scrollable scrollable ) {
    this.scrollable = scrollable;
  }

  void layout( LayoutContext context ) {
    scrollable.setLocation( computeLocation( context ) );
    scrollable.setSize( computeWidth( context ), computeHeight( context ) );
  }

  private static Point computeLocation( LayoutContext context ) {
    Point origin = context.getLocation();
    return new Point( origin.x - context.getOffset(), origin.y - context.getOffset() );
  }

  private static int computeWidth( LayoutContext context ) {
    int result = max( context.getPreferredSize().x, context.getVisibleArea().width );
    if( context.isVerticalBarVisible() ) {
      result = computeWidthWithVerticalBarPadding( context );
    }
    return result + context.getOffset() * 2;
  }

  private static int computeWidthWithVerticalBarPadding( LayoutContext context ) {
    int preferredWidth = context.getPreferredSize().x;
    int visibleAreaWidth = context.getVisibleArea().width;
    int offset = context.getVerticalBarOffset();
    return max( preferredWidth + offset, visibleAreaWidth + offset );
  }

  private static int computeHeight( LayoutContext context ) {
    int result = context.getVisibleArea().height;
    if( context.isHorizontalBarVisible() ) {
      result = computeHeightWithHorizontalBarPadding( context );
    }
    return result + context.getOffset() * 2;
  }

  private static int computeHeightWithHorizontalBarPadding( LayoutContext context ) {
    return context.getVisibleArea().height - BAR_BREADTH;
  }
}