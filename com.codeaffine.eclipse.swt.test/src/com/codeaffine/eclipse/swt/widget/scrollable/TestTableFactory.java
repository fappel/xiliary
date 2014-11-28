package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.TableHelper.createTable;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

class TestTableFactory implements ScrollableFactory<Table> {

  private Table table;

  @Override
  public Table create( Composite parent ) {
    table = createTable( parent, 20 );
    return table;
  }

  Table getTable() {
    return table;
  }
}