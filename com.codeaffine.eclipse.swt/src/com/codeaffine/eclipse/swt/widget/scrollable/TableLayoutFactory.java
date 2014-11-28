package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Table;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TableLayoutFactory extends ScrollableLayoutFactory<Table> {

  static class TableLayoutContextFactory implements LayoutContextFactory {

    private final Table table;

    TableLayoutContextFactory( Table table ) {
      this.table = table;
    }

    @Override
    public LayoutContext create() {
      return new LayoutContext( table, table.getItemHeight() );
    }
  }

  @Override
  public Layout create( Table table, FlatScrollBar horizontal, FlatScrollBar vertical ) {
    return new ScrollableLayout<Table>( table, createContextFactory( table ), horizontal, vertical );
  }

  @Override
  public SelectionListener createHorizontalSelectionListener( Table table ) {
    return new HorizontalSelectionListener( table );
  }

  @Override
  public SelectionListener createVerticalSelectionListener( Table table ) {
    return new TableVerticalSelectionListener( table );
  }

  @Override
  public DisposeListener createWatchDog( Table table, FlatScrollBar horizontal, FlatScrollBar vertical ) {
    return new WatchDog( table, createContextFactory( table ), new TableVerticalScrollBarUpdater( table, vertical ) );
  }

  private static TableLayoutContextFactory createContextFactory( Table table ) {
    return new TableLayoutContextFactory( table );
  }
}