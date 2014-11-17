package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Tree;

class TreeLayoutContext {

  static final int OVERLAY_OFFSET = 30;

  private final boolean horizontalBarVisible;
  private final boolean verticalBarVisible;
  private final Rectangle visibleArea;
  private final int verticalBarOffset;
  private final Point preferredSize;

  TreeLayoutContext( Tree tree ) {
    preferredSize = tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
    visibleArea = tree.getParent().getClientArea();
    verticalBarVisible = preferredSize.y > visibleArea.height;
    horizontalBarVisible = preferredSize.x > visibleArea.width;
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

  private static int computeVerticalBarOffset( Tree tree ) {
    int verticalScrollBarWidth = tree.getVerticalBar().getSize().x;
    if( verticalScrollBarWidth == 0 ) {
      verticalScrollBarWidth = OVERLAY_OFFSET;
    }
    return verticalScrollBarWidth - BAR_BREADTH;
  }
}