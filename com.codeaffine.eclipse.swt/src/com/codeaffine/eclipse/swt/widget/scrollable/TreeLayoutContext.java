package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Tree;

class TreeLayoutContext {

  private final boolean horizontalBarVisible;
  private final boolean verticalBarVisible;
  private final Rectangle visibleArea;
  private final Point preferredSize;

  TreeLayoutContext( Tree tree ) {
    preferredSize = tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
    visibleArea = tree.getParent().getClientArea();
    verticalBarVisible = tree.getVerticalBar().isVisible();
    horizontalBarVisible = preferredSize.x > visibleArea.width;
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
}