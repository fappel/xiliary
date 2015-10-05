package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.MAX_EXPANSION;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.BAR_BREADTH;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Label;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
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

  void layout( AdaptionContext<?> context ) {
    layoutVertical( context );
    layoutHorizontal( context );
    layoutCornerOverlay( context );
  }

  private void layoutVertical( AdaptionContext<?> context ) {
    if( context.isVerticalBarVisible() ) {
      int x = context.getVisibleArea().width - BAR_BREADTH - borderOffset( context );
      int height = computeVerticalHeight( context );
      vertical.setBounds( x, 0, BAR_BREADTH, height );
      vertical.setVisible( true );
    } else {
      vertical.setVisible( false );
      vertical.setBounds( 0, 0, 0, 0 );
    }
  }

  private static int computeVerticalHeight( AdaptionContext<?> context ) {
    Rectangle visibleArea = context.getVisibleArea();
    int baseHeight = context.isHorizontalBarVisible() ? visibleArea.height - MAX_EXPANSION  : visibleArea.height;
    return baseHeight - borderOffset( context );
  }

  private void layoutHorizontal( AdaptionContext<?> context ) {
    if( context.isHorizontalBarVisible() ) {
      int y = context.getVisibleArea().height - BAR_BREADTH - borderOffset( context );
      int width = computeHorizontalWidth( context );
      horizontal.setBounds( 0, y, width, BAR_BREADTH );
      horizontal.setVisible( true );
    } else {
      horizontal.setVisible( false );
      horizontal.setBounds( 0, 0, 0, 0 );
    }
  }

  private static int computeHorizontalWidth( AdaptionContext<?> context ) {
    Rectangle visibleArea = context.getVisibleArea();
    int baseWidth = context.isVerticalBarVisible() ? visibleArea.width - MAX_EXPANSION : visibleArea.width;
    return baseWidth - borderOffset( context ) ;
  }

  private void layoutCornerOverlay( AdaptionContext<?> context ) {
    cornerOverlay.setBounds( calculateCornerOverlayBounds( horizontal, vertical, context ) );
  }

  static Rectangle calculateCornerOverlayBounds(
    FlatScrollBar horizontal, FlatScrollBar vertical, AdaptionContext<?> context )
  {
    Point hSize = horizontal.getSize();
    Point vSize = vertical.getSize();
    int borderWidth = context.getBorderWidth();
    return new Rectangle( hSize.x, vSize.y, vSize.x + context.getOffset() + borderWidth, hSize.y + borderWidth );
  }

  private static int borderOffset( AdaptionContext<?> context ) {
    return context.getBorderWidth() * 2;
  }
}