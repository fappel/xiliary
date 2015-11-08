package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.Reconciliation;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class ScrollableLayout extends Layout {

  private final ScrollBarConfigurer horizontalBarConfigurer;
  private final ScrollableLayouter scrollableLayouter;
  private final OverlayLayouter overlayLayouter;
  private final Reconciliation reconciliation;
  private final AdaptionContext<?> context;

  ScrollableLayout( AdaptionContext<?> context, FlatScrollBar horizontal, FlatScrollBar vertical, Label cornerOverlay ) {
    this( context,
          new OverlayLayouter( horizontal, vertical, cornerOverlay ),
          new ScrollableLayouter( context ),
          new ScrollBarConfigurer( horizontal ),
          context.getReconciliation() );
  }

  ScrollableLayout( AdaptionContext<?> context,
                    OverlayLayouter overlayLayouter,
                    ScrollableLayouter scrollableLayouter,
                    ScrollBarConfigurer horizontalBarConfigurer,
                    Reconciliation reconciliation  )
  {
    this.context = context;
    this.overlayLayouter = overlayLayouter;
    this.scrollableLayouter = scrollableLayouter;
    this.horizontalBarConfigurer = horizontalBarConfigurer;
    this.reconciliation = reconciliation;
  }

  @Override
  protected void layout( Composite composite, boolean flushCache ) {
    reconciliation.runWhileSuspended( () -> layout() );
  }

  @Override
  protected Point computeSize( Composite composite, int wHint, int hHint, boolean flushCache ) {
    return context.getScrollable().computeSize( wHint, hHint, flushCache );
  }

  private void layout() {
    AdaptionContext<?> context = this.context.newContext();
    scrollableLayouter.layout( context );
    overlayLayouter.layout( this.context.newContext() );
    if( context.isHorizontalBarVisible() ) {
      horizontalBarConfigurer.configure( context );
    }
  }
}