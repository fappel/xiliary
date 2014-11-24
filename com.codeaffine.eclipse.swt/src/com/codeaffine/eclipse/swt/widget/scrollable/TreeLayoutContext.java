package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Tree;

class TreeLayoutContext {

  static final int OVERLAY_OFFSET = 40;

  private final boolean horizontalBarVisible;
  private final boolean verticalBarVisible;
  private final Rectangle visibleArea;
  private final int verticalBarOffset;
  private final Point preferredSize;

  TreeLayoutContext( Tree tree ) {
    preferredSize = tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
    visibleArea = tree.getParent().getClientArea();
    horizontalBarVisible = preferredSize.x > visibleArea.width;
    verticalBarVisible = computeVerticalBarVisible( tree, horizontalBarVisible, preferredSize.y, visibleArea.height );
    verticalBarOffset = computeVerticalBarOffset( tree );
  }

  boolean isVerticalBarVisible() {
    return verticalBarVisible;
  }

  boolean isHorizontalBarVisible() {
    return horizontalBarVisible;
  }

  Point getPreferredSize() {
    return preferredSize;
  }

  Rectangle getVisibleArea() {
    return visibleArea;
  }

  int getVerticalBarOffset() {
    return verticalBarOffset;
  }

  private static boolean computeVerticalBarVisible(
    Tree tree, boolean horizontalBarVisible, int preferredHeight, int visibleAreaHeight )
  {
    boolean result;
    if( !horizontalBarVisible ) {
      result = computeVisibleItemsHeight( tree, preferredHeight ) >= visibleAreaHeight;
    } else {
      result = computeVisibleItemsHeight( tree, preferredHeight ) + BAR_BREADTH - 1 >= visibleAreaHeight;
    }
    return result;
  }

  private static int computeVisibleItemsHeight( Tree tree, int preferredHeight ) {
    return ( preferredHeight / tree.getItemHeight() ) * tree.getItemHeight();
  }

  private static int computeVerticalBarOffset( Tree tree ) {
    int result = tree.getVerticalBar().getSize().x;
    if( result == 0 ) {
      result = OVERLAY_OFFSET;
    }
    return result;
  }
}