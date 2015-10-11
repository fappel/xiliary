package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static java.lang.Integer.valueOf;
import static java.lang.Math.max;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;

class SizeComputer {

  static final String WIDTH_OFFSET_ON_VISIBLE_HORIZONTAL_BAR = AdaptionContext.class.getName() + "#widthAdjustment:";
  static final String PREFERRED_SIZE = AdaptionContext.class.getName() + "# preferredSize";

  private final Scrollable scrollable;
  private final Composite adapter;

  SizeComputer( Scrollable scrollable, Composite adapter ) {
    this.scrollable = scrollable;
    this.adapter = adapter;
  }

  Point getPreferredSize() {
    Point preferredSize = getPreferredSizeInternal();
    return new Point( preferredSize.x + getPreferredWidthAdjustmentForVisibleHorizontalBar(), preferredSize.y );
  }

  void updatePreferredSize() {
    Point computed = scrollable.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
    if( computed.x - scrollable.getVerticalBar().getSize().x == scrollable.getSize().x ) {
      scrollable.setData( PREFERRED_SIZE, scrollable.getSize() );
    } else {
      int parentWidth = adapter.getClientArea().width;
      ScrollBar horizontalBar = scrollable.getHorizontalBar();
      int width = max( parentWidth, horizontalBar.getMaximum() );
      scrollable.setData( PREFERRED_SIZE, new Point( width, computed.y ) );
    }
  }

  void adjustPreferredWidthIfHorizontalBarIsVisible() {
    ScrollBar horizontalBar = scrollable.getHorizontalBar();
    if( horizontalBar.isVisible() && adapter.getParent() == scrollable.getParent() ) {
      updatePreferredSize();
      scrollable.setData( getWidthOffsetKey(), valueOf( getPreferredSizeInternal().x ) );
    }
  }

  private Point getPreferredSizeInternal() {
    if( getBufferedPreferredSize() == null ) {
      updatePreferredSize();
    }
    return getBufferedPreferredSize();
  }

  private Point getBufferedPreferredSize() {
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