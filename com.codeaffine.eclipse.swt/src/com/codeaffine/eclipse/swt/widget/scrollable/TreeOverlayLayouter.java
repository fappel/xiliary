package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;

import org.eclipse.swt.graphics.Rectangle;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TreeOverlayLayouter {

  private final FlatScrollBar horizontal;
  private final FlatScrollBar vertical;

  TreeOverlayLayouter( FlatScrollBar horizontal, FlatScrollBar vertical ) {
    this.horizontal = horizontal;
    this.vertical = vertical;
  }

  void layout( TreeLayoutContext context ) {
    layoutVertical( context );
    layoutHorizontal( context );
  }

  private void layoutVertical( TreeLayoutContext context ) {
    if( context.isVerticalBarVisible() ) {
      Rectangle visibleArea = context.getVisibleArea();
      int vHeight = context.isHorizontalBarVisible() ? visibleArea.height - BAR_BREADTH : visibleArea.height;
      vertical.setBounds( visibleArea.width - BAR_BREADTH, 0, BAR_BREADTH, vHeight );
      vertical.setVisible( true );
    } else {
      vertical.setVisible( false );
      vertical.setBounds( 0, 0, 0, 0 );
    }
  }

  private void layoutHorizontal( TreeLayoutContext context ) {
    if( context.isHorizontalBarVisible() ) {
      Rectangle visibleArea = context.getVisibleArea();
      int hWidth = context.isVerticalBarVisible() ? visibleArea.width - BAR_BREADTH : visibleArea.width;
      horizontal.setBounds( 0, visibleArea.height - BAR_BREADTH, hWidth, BAR_BREADTH );
      horizontal.setVisible( true );
    } else {
      horizontal.setVisible( false );
      horizontal.setBounds( 0, 0, 0, 0 );
    }
  }
}