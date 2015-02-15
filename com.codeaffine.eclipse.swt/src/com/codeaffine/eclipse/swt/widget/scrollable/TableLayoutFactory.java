package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Table;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TableLayoutFactory extends ScrollableLayoutFactory<Table> {

  @Override
  public Layout create(
    LayoutContext<Table> context, FlatScrollBar horizontal, FlatScrollBar vertical, Label cornerOverlay )
  {
    return new ScrollableLayout( newContext( context ), horizontal, vertical, cornerOverlay );
  }

  @Override
  public SelectionListener createHorizontalSelectionListener( LayoutContext<Table> context ) {
    return new HorizontalSelectionListener( context );
  }

  @Override
  public SelectionListener createVerticalSelectionListener( LayoutContext<Table> context ) {
    return new TableVerticalSelectionListener( context );
  }

  @Override
  public DisposeListener createWatchDog(
    LayoutContext<Table> context, FlatScrollBar horizontal, FlatScrollBar vertical )
  {
    TableVerticalScrollBarUpdater updater = new TableVerticalScrollBarUpdater( context.getScrollable(), vertical );
    return new WatchDog( newContext( context ), updater );
  }

  private static LayoutContext<Table> newContext( LayoutContext<Table> context ) {
    return context.newContext( context.getScrollable().getItemHeight() );
  }
}