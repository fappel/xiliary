package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class ScrollableLayout<T extends Scrollable> extends Layout {

  private final ScrollBarConfigurer horizontalBarConfigurer;
  private final ScrollableLayouter scrollableLayouter;
  private final LayoutContextFactory contextFactory;
  private final OverlayLayouter overlayLayouter;
  private final T scrollable;

  ScrollableLayout( T scrollable,
                    LayoutContextFactory contextFactory,
                    FlatScrollBar horizontal,
                    FlatScrollBar vertical,
                    Label cornerOverlay )
  {
    this( scrollable,
          contextFactory,
          new OverlayLayouter( horizontal, vertical, cornerOverlay ),
          new ScrollableLayouter( scrollable ),
          new ScrollBarConfigurer( horizontal ) );
  }

  ScrollableLayout( T scrollable ,
                    LayoutContextFactory contextFactory ,
                    OverlayLayouter overlayLayouter ,
                    ScrollableLayouter scrollableLayouter ,
                    ScrollBarConfigurer horizontalBarConfigurer  )
  {
    this.scrollable = scrollable;
    this.contextFactory = contextFactory;
    this.overlayLayouter = overlayLayouter;
    this.scrollableLayouter = scrollableLayouter;
    this.horizontalBarConfigurer = horizontalBarConfigurer;
  }

  @Override
  protected void layout( Composite composite, boolean flushCache ) {
    LayoutContext context = contextFactory.create();
    overlayLayouter.layout( context );
    scrollableLayouter.layout( context );
    if( context.isHorizontalBarVisible() ) {
      horizontalBarConfigurer.configure( context );
    }
  }

  @Override
  protected Point computeSize( Composite composite, int wHint, int hHint, boolean flushCache ) {
    return scrollable.computeSize( wHint, hHint, flushCache );
  }

  protected T getScrollable() {
    return scrollable;
  }
}