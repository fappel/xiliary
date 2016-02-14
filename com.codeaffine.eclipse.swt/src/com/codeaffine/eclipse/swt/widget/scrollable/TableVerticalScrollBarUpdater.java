package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TableVerticalScrollBarUpdater implements ScrollBarUpdater {

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
    scrollBar.setSelection( calculateSelection() );
  }

  private int calculateSelection() {
    if( table.getItemCount() > 0 ) {
      int topIndex = table.getTopIndex();
      return cornerCaseWorkaroundForGtk( topIndex, table.getItem( topIndex ) ) * SELECTION_RASTER_SMOOTH_FACTOR;
    }
    return 0;
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

  // [fappel]: setting topItemIndex does not work reliable on gtk for last selection items
  // if top item is only half visible. The table is moved correctly but top item index returns the old value.
  // This recognizes such a situation and increases the flat scrollbar selection anyway.
  private static int cornerCaseWorkaroundForGtk( int selection, TableItem topItem ) {
    return topItem != null && topItem.getBounds().y < 0 ? selection + 1 : selection;
  }
}