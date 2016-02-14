package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class StructuredScrollableHorizontalSelectionListener extends SelectionAdapter {

  private final AdaptionContext<?> context;

  StructuredScrollableHorizontalSelectionListener( AdaptionContext<?> context ) {
    this.context = context;
  }

  @Override
  public void widgetSelected( SelectionEvent event ) {
    updateLocation( new Point( -getSelection( event ), computeHeight() ) );
  }

  private int computeHeight() {
    if( context.isScrollableReplacedByAdapter() ) {
      return -context.getBorderWidth();
    }
    return context.getScrollable().getLocation().y - context.getBorderWidth();
  }

  private int getSelection( SelectionEvent event ) {
    return ( ( FlatScrollBar )event.widget ).getSelection() + context.getBorderWidth();
  }

  private void updateLocation( final Point result ) {
    context.getReconciliation().runWhileSuspended( () -> context.getScrollable().setLocation( result ) );
  }
}