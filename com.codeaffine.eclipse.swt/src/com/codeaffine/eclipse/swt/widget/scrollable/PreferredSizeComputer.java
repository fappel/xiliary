package com.codeaffine.eclipse.swt.widget.scrollable;

import static java.lang.Integer.valueOf;
import static java.lang.Math.max;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;

class PreferredSizeComputer {

  static final String WIDTH_OFFSET_ON_VISIBLE_HORIZONTAL_BAR = LayoutContext.class.getName() + "#widthAdjustment:";
  static final String PREFERRED_SIZE = LayoutContext.class.getName() + "# preferredSize";
  static final int WIDTH_BUFFER = 2;

  private final Scrollable scrollable;
  private final Composite adapter;

  PreferredSizeComputer( Scrollable scrollable, Composite adapter ) {
    this.scrollable = scrollable;
    this.adapter = adapter;
  }

  Point getPreferredSize() {
    Point preferredSize = getPreferredSizeInternal();
    return new Point( preferredSize.x + getPreferredWidthAdjustmentForVisibleHorizontalBar(), preferredSize.y );
  }

  void updatePreferredSize() {
    Point computed = scrollable.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
    int parentWidth = adapter.getClientArea().width;
    int width = max( parentWidth, computed.x + WIDTH_BUFFER );
    scrollable.setData( PREFERRED_SIZE, new Point( width, computed.y ) );
  }

  void adjustPreferredWidthIfHorizontalBarIsVisible() {
    ScrollBar horizontalBar = scrollable.getHorizontalBar();
    if( horizontalBar.isVisible() && adapter.getParent() == scrollable.getParent() ) {
      int adjustment = getPreferredSizeInternal().x;
      scrollable.setData( getWidthOffsetKey(), valueOf( adjustment ) );
    }
  }

  private Point getPreferredSizeInternal() {
    if( scrollable.getData( PREFERRED_SIZE ) == null ) {
      updatePreferredSize();
    }
    return ( Point )scrollable.getData( PREFERRED_SIZE );
  }

  private int getPreferredWidthAdjustmentForVisibleHorizontalBar() {
    Integer adjustment = ( Integer )scrollable.getData( getWidthOffsetKey() );
    if( adjustment != null ) {
      return adjustment.intValue();
    }
    return 0;
  }

  private String getWidthOffsetKey() {
    return WIDTH_OFFSET_ON_VISIBLE_HORIZONTAL_BAR + getPreferredSizeInternal();
  }
}