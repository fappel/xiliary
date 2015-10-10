package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

class TableHelper {

  private static final String[] HEADER_TITLES = {" ", "Name", "Description" };

  static Table createPackedSingleColumnTable( Composite parent, int itemCount ) {
    Table result = new Table( parent, SWT.SINGLE | SWT.FULL_SELECTION );
    result.setHeaderVisible( true );
    result.setLinesVisible( true );
    TableColumn column = new TableColumn( result, SWT.NONE );
    column.setResizable( false );
    column.setText( "Header" );
    for( int i = 0; i < itemCount; i++ ) {
      TableItem item = new TableItem( result, SWT.NONE );
      item.setText( "This text is the very important description of item_" + i + "." );
      item.setImage( new Image( parent.getDisplay(), new Rectangle( 0, 0, 20, 20 ) ) );
    }
    result.setSelection( 0 );
    column.pack();
    result.pack();
    return result;
  }

  static Table createTable( Composite parent, int itemCount ) {
    Table result = new Table( parent, SWT.NONE );
    createHeaders( result );
    createItems( result, "table-item_", itemCount );
    return result;
  }

  private static void createHeaders( Table parent ) {
    parent.setHeaderVisible( true );
    parent.setLinesVisible( true );
    for( int i = 0; i < HEADER_TITLES.length; i++ ) {
      TableColumn column = new TableColumn( parent, SWT.NONE );
      column.setText( HEADER_TITLES[ i ] );
    }
  }

  private static void createItems( Table parent, String name, int itemCount ) {
    for( int i = 0; i < itemCount; i++ ) {
      TableItem item = new TableItem( parent, SWT.NONE );
      for( int j = 0; j < HEADER_TITLES.length; j++ ) {
        item.setText( 0, String.valueOf( i ) );
        item.setText( 1, name + i );
        item.setText( 2, "This text is the very important description of" + name + i + "." );
      }
    }
    TableColumn[] columns = parent.getColumns();
    for (TableColumn tableColumn : columns) {
      tableColumn.pack();
    }
  }
}