package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;
import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.MAX_EXPANSION;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Label;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class OverlayLayouter {

  private final FlatScrollBar horizontal;
  private final FlatScrollBar vertical;
  private final Label cornerOverlay;

  OverlayLayouter( FlatScrollBar horizontal, FlatScrollBar vertical, Label cornerOverlay  ) {
    this.cornerOverlay = cornerOverlay;
    this.horizontal = horizontal;
    this.vertical = vertical;
  }

  void layout( LayoutContext context ) {
    layoutVertical( context );
    layoutHorizontal( context );
    layoutCornerOverlay( context );
  }

  private void layoutVertical( LayoutContext context ) {
    if( context.isVerticalBarVisible() ) {
      Rectangle visibleArea = context.getVisibleArea();
      int vHeight = context.isHorizontalBarVisible() ? visibleArea.height - MAX_EXPANSION: visibleArea.height;
      vertical.setBounds( visibleArea.width - BAR_BREADTH, 0, BAR_BREADTH, vHeight );
      vertical.setVisible( true );
    } else {
      vertical.setVisible( false );
      vertical.setBounds( 0, 0, 0, 0 );
    }
  }

  private void layoutHorizontal( LayoutContext context ) {
    if( context.isHorizontalBarVisible() ) {
      Rectangle visibleArea = context.getVisibleArea();
      int hWidth = context.isVerticalBarVisible() ? visibleArea.width - MAX_EXPANSION : visibleArea.width;
      horizontal.setBounds( 0, visibleArea.height - BAR_BREADTH, hWidth, BAR_BREADTH );
      horizontal.setVisible( true );
    } else {
      horizontal.setVisible( false );
      horizontal.setBounds( 0, 0, 0, 0 );
    }
  }

  private void layoutCornerOverlay( LayoutContext context ) {
    cornerOverlay.setBounds( calculateCornerOverlayBounds( horizontal, vertical, context ) );
  }

  static Rectangle calculateCornerOverlayBounds( FlatScrollBar horizontal, FlatScrollBar vertical, LayoutContext context  ) {
    Point hSize = horizontal.getSize();
    Point vSize = vertical.getSize();
    return new Rectangle( hSize.x, vSize.y, vSize.x + context.getOffset(), hSize.y );
  }

}