package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TableVerticalSelectionListener extends SelectionAdapter {

  private final Table table;

  TableVerticalSelectionListener( Table table ) {
    this.table = table;
  }

  @Override
  public void widgetSelected( SelectionEvent event ) {
    table.setTopIndex( ( ( FlatScrollBar )event.widget ).getSelection() );
  }
}