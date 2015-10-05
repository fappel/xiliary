package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.BAR_BREADTH;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.util.Platform;
import com.codeaffine.eclipse.swt.util.Platform.PlatformType;

class ScrollbarVisibility {

  private final boolean horizontalBarVisible;
  private final boolean verticalBarVisible;

  ScrollbarVisibility( SizeComputer sizeComputer, Scrollable scrollable, Rectangle clientArea, int itemHeight ) {
    horizontalBarVisible =    isWin32() && scrollable.getHorizontalBar().isVisible()
                           || computeHorizontalBarVisible( sizeComputer, clientArea );
    verticalBarVisible =    isWin32() && scrollable.getVerticalBar().isVisible()
                         || computeVerticalBarVisible( sizeComputer, clientArea, itemHeight );
  }

  boolean isHorizontalBarVisible() {
    return horizontalBarVisible;
  }

  boolean isVerticalBarVisible() {
    return verticalBarVisible;
  }

  private static boolean isWin32() {
    return new Platform().matches( PlatformType.WIN32 );
  }

  private static boolean computeHorizontalBarVisible( SizeComputer sizeComputer, Rectangle clientArea ) {
    Point preferredSize = sizeComputer.getPreferredSize();
    return preferredSize.x > clientArea.width;
  }

  private boolean computeVerticalBarVisible( SizeComputer sizeComputer, Rectangle clientArea, int itemHeight ) {
    int preferredHeight = sizeComputer.getPreferredSize().y;
    int clientAreaHeight = clientArea.height;
    return computeVerticalBarVisible( horizontalBarVisible, preferredHeight, clientAreaHeight, itemHeight );
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
}