package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType;

class LayoutContext {

  static final int OVERLAY_OFFSET = 40;
  static final int WIDTH_BUFFER = 2;
  static final int OFFSET = new Platform().matches( PlatformType.GTK ) ? 2 : 0;

  private final boolean horizontalBarVisible;
  private final boolean verticalBarVisible;
  private final Rectangle visibleArea;
  private final int verticalBarOffset;
  private final Point preferredSize;
  private final Point location;

  LayoutContext( Scrollable scrollable, int itemHeight ) {
    Point computed = scrollable.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
    preferredSize = new Point( computed.x + OFFSET + WIDTH_BUFFER, computed.y + OFFSET );
    visibleArea = scrollable.getParent().getClientArea();
    location = new Point( visibleArea.x - OFFSET, visibleArea.y - OFFSET );
    horizontalBarVisible = preferredSize.x > visibleArea.width;
    verticalBarOffset = computeVerticalBarOffset( scrollable );
    verticalBarVisible
      = computeVerticalBarVisible( horizontalBarVisible, preferredSize.y, visibleArea.height, itemHeight );
  }

  Point getLocation() {
    return location;
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
    boolean horizontalBarVisible, int preferredHeight, int visibleAreaHeight, int itemHeight  )
  {
    boolean result;
    if( !horizontalBarVisible ) {
      result = computeVisibleItemsHeight( preferredHeight, itemHeight ) >= visibleAreaHeight;
    } else {
      result = computeVisibleItemsHeight( preferredHeight, itemHeight ) + BAR_BREADTH - 1 >= visibleAreaHeight;
    }
    return result;
  }

  private static int computeVisibleItemsHeight( int preferredHeight, int itemHeight  ) {
    return ( preferredHeight / itemHeight ) * itemHeight;
  }

  private static int computeVerticalBarOffset( Scrollable scrollable ) {
    int result = scrollable.getVerticalBar().getSize().x;
    if( result == 0 ) {
      result = OVERLAY_OFFSET;
    }
    return result;
  }
}