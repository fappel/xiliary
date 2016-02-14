package com.codeaffine.eclipse.swt.widget.scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class StructuredScrollableHorizontalScrollBarUpdater implements ScrollBarUpdater {

  private final AdaptionContext<?> context;
  private final FlatScrollBar scrollBar;

  StructuredScrollableHorizontalScrollBarUpdater( AdaptionContext<?> context, FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
    this.context = context;
  }

  @Override
  public void update() {
    AdaptionContext<?> newContext = context.newContext();
    scrollBar.setIncrement( 1 );
    scrollBar.setMaximum( newContext.getPreferredSize().x );
    scrollBar.setMinimum( newContext.getOffset() );
    scrollBar.setPageIncrement( newContext.getVisibleArea().width );
    scrollBar.setThumb( newContext.getVisibleArea().width );
  }
}