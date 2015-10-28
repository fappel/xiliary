package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Table;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TableLayoutFactory extends ScrollableLayoutFactory<Table> {

  @Override
  public Layout create( AdaptionContext<Table> context, FlatScrollBar horizontal, FlatScrollBar vertical ) {
    return new ScrollableLayout( newContext( context ), horizontal, vertical, getCornerOverlay() );
  }

  @Override
  public SelectionListener createHorizontalSelectionListener( AdaptionContext<Table> context ) {
    return new HorizontalSelectionListener( context );
  }

  @Override
  public SelectionListener createVerticalSelectionListener( AdaptionContext<Table> context ) {
    return new TableVerticalSelectionListener( context );
  }

  @Override
  public DisposeListener createWatchDog(
    AdaptionContext<Table> context, FlatScrollBar horizontal, FlatScrollBar vertical )
  {
    TableVerticalScrollBarUpdater updater = new TableVerticalScrollBarUpdater( context.getScrollable(), vertical );
    return new WatchDog( newContext( context ), updater );
  }

  private static AdaptionContext<Table> newContext( AdaptionContext<Table> context ) {
    return context.newContext( context.getScrollable().getItemHeight() );
  }
}