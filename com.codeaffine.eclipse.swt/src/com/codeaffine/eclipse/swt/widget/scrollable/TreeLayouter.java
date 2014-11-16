package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;
import static java.lang.Math.max;

import org.eclipse.swt.widgets.Tree;

class TreeLayouter {

  private final Tree tree;

  TreeLayouter( Tree tree ) {
    this.tree = tree;
  }

  void layout( TreeLayoutContext context ) {
    tree.setLocation( context.getVisibleArea().x, context.getVisibleArea().y );
    tree.setSize( computeWidth( context ), computeHeight( context ) );
  }

  private int computeWidth( TreeLayoutContext context ) {
    int result = max( context.getPreferredSize().x, context.getVisibleArea().width );
    if( context.isVerticalBarVisible() ) {
      result = computeWidthWithVerticalBarPadding( context );
    }
    return result;
  }

  private int computeWidthWithVerticalBarPadding( TreeLayoutContext context ) {
    int offset = tree.getVerticalBar().getSize().x - BAR_BREADTH;
    int preferredWidth = context.getPreferredSize().x;
    int visibleAreaWidth = context.getVisibleArea().width;
    return max( preferredWidth + offset, visibleAreaWidth + offset );
  }

  private static int computeHeight( TreeLayoutContext context ) {
    int result = context.getVisibleArea().height;
    if( context.isHorizontalBarVisible() ) {
      result = computeHeightWithHorizontalBarPadding( context );
    }
    return result;
  }

  private static int computeHeightWithHorizontalBarPadding( TreeLayoutContext context ) {
    return context.getVisibleArea().height - BAR_BREADTH;
  }
}