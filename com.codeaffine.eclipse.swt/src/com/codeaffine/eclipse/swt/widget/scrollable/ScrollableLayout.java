package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class ScrollableLayout extends Layout {

  private final ScrollBarConfigurer horizontalBarConfigurer;
  private final ScrollableLayouter scrollableLayouter;
  private final OverlayLayouter overlayLayouter;
  private final LayoutContext<?> context;

  ScrollableLayout( LayoutContext<?> context, FlatScrollBar horizontal, FlatScrollBar vertical, Label cornerOverlay ) {
    this( context,
          new OverlayLayouter( horizontal, vertical, cornerOverlay ),
          new ScrollableLayouter( context.getScrollable() ),
          new ScrollBarConfigurer( horizontal ) );
  }

  ScrollableLayout( LayoutContext<?> context,
                    OverlayLayouter overlayLayouter,
                    ScrollableLayouter scrollableLayouter,
                    ScrollBarConfigurer horizontalBarConfigurer )
  {
    this.context = context;
    this.overlayLayouter = overlayLayouter;
    this.scrollableLayouter = scrollableLayouter;
    this.horizontalBarConfigurer = horizontalBarConfigurer;
  }

  @Override
  protected void layout( Composite composite, boolean flushCache ) {
    LayoutContext<?> context = this.context.newContext();
    overlayLayouter.layout( context );
    scrollableLayouter.layout( context );
    if( context.isHorizontalBarVisible() ) {
      horizontalBarConfigurer.configure( context );
    }
  }

  @Override
  protected Point computeSize( Composite composite, int wHint, int hHint, boolean flushCache ) {
    return context.getScrollable().computeSize( wHint, hHint, flushCache );
  }
}