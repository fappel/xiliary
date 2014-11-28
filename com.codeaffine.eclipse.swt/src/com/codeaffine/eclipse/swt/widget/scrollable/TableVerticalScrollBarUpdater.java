package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.Table;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TableVerticalScrollBarUpdater implements VerticalScrollBarUpdater {

  private final FlatScrollBar scrollBar;
  private final Table table;

  TableVerticalScrollBarUpdater( Table table, FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
    this.table = table;
  }

  @Override
  public void update() {
    int itemCount = table.getItemCount();
    scrollBar.setIncrement( 1 );
    scrollBar.setMaximum( itemCount );
    scrollBar.setMinimum( 0 );
    scrollBar.setPageIncrement( calculateThumb() );
    scrollBar.setThumb( calculateThumb() );
    scrollBar.setSelection( table.getTopIndex() );
  }

  int calculateThumb() {
    return table.getClientArea().height / table.getItemHeight();
  }
}