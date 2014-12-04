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
    scrollBar.setIncrement( 1 * SELECTION_RASTER_SMOOTH_FACTOR );
    scrollBar.setMaximum( itemCount * SELECTION_RASTER_SMOOTH_FACTOR );
    scrollBar.setMinimum( 0 );
    scrollBar.setPageIncrement( calculateThumb() );
    scrollBar.setThumb( calculateThumb() );
    scrollBar.setSelection( table.getTopIndex() * SELECTION_RASTER_SMOOTH_FACTOR );
  }

  int calculateThumb() {
    int height = calculateHeight();
    return SELECTION_RASTER_SMOOTH_FACTOR * ( height / table.getItemHeight() );
  }

  int calculateHeight() {
    int result = table.getClientArea().height;
    if( table.getHeaderVisible() ) {
      result -= table.getHeaderHeight();
    }
    return result;
  }
}