package com.codeaffine.eclipse.swt.widget.scrollable;

import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

class TableHelper {

  static final String[] HEADER_TITLES = {" ", "Name", "Description" };

  static Table createTableInSingleCellGridLayout( Shell shell, Consumer<Table> consumer ) {
    GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    shell.setLayout( layout );
    shell.setLocation( shell.getLocation() );
    Table result = new Table( shell, SWT.H_SCROLL | SWT.V_SCROLL );
    consumer.accept( result );
    equipTableWithTwoItems( result);
    return result;
  }

  private static void equipTableWithTwoItems( Table table ) {
    table.setBackground( table.getDisplay().getSystemColor( SWT.COLOR_INFO_BACKGROUND ) );
    table.setLocation( 0, 0 );
    createItem( table, "first" );
    createItem( table, "Second" );
    table.setLayoutData( computeLayoutData( table ) );
  }

  private static GridData computeLayoutData( Table table ) {
    GridData result = new GridData( GridData.FILL_BOTH );
    result.widthHint = table.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x;
    result.heightHint = table.getItemHeight() * table.getItemCount();
    return result;
  }

  static Table createVirtualTableWithOwnerDrawnItems( Composite parent, ItemList itemList ) {
    Table result = new Table( parent, SWT.BORDER | SWT.VIRTUAL );
    result.addListener( SWT.MeasureItem, evt -> { evt.height = 24; } );
    result.addListener( SWT.EraseItem, evt -> {} );
    result.addListener( SWT.PaintItem, evt -> {} );
    result.addListener( SWT.SetData, event -> fetchPage( itemList, result, event ) );
    itemList.fetchPage();
    return result;
  }

  private static void fetchPage( ItemList itemList, Table table, Event event ) {
    TableItem item = ( TableItem )event.item;
    item.setText( itemList.getItems()[ event.index ] );
    if( event.index > table.getItemCount() - 3 ) {
      itemList.fetchPage();
      table.setItemCount( itemList.getItems().length );
    }
  }

  static Table createPackedSingleColumnTable( Composite parent, int itemCount ) {
    Table result = new Table( parent, SWT.SINGLE | SWT.FULL_SELECTION );
    result.setHeaderVisible( true );
    result.setLinesVisible( true );
    TableColumn column = new TableColumn( result, SWT.NONE );
    column.setResizable( false );
    column.setText( "Header" );
    for( int i = 0; i < itemCount; i++ ) {
      TableItem item = createItem( result, "This text is the very important description of item_" + i + "." );
      item.setImage( new Image( parent.getDisplay(), new Rectangle( 0, 0, 20, 20 ) ) );
    }
    result.setSelection( 0 );
    column.pack();
    result.pack();
    return result;
  }

  private static TableItem createItem( Table table, String text ) {
    TableItem result = new TableItem( table, SWT.NONE );
    result.setText( text );
    return result;
  }

  static Table createTable( Composite parent, int itemCount ) {
    Table result = new Table( parent, SWT.NONE );
    result.setLinesVisible( true );
    createHeaders( result );
    createItems( result, "table-item_", itemCount );
    return result;
  }

  private static void createHeaders( Table parent ) {
    parent.setHeaderVisible( true );
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