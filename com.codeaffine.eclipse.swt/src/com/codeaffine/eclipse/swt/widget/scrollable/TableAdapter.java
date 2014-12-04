package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType.WIN32;
import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.createLayoutFactory;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;

public class TableAdapter extends Table implements Adapter<Table>, DisposeListener, ScrollbarStyle {

  private LayoutFactory<Table> layoutFactory;
  private Reconciliation reconciliation;
  private LayoutContext<Table> context;
  private Table table;

  TableAdapter() {
    super( null, -1 );
  }

  @Override
  @SuppressWarnings("unchecked")
  public void adapt( Table table ) {
    Platform platform = new Platform();
    this.layoutFactory = createLayoutFactory( platform, createLayoutMapping() );
    this.table = table;
    if( platform.matchesOneOf( WIN32 ) ) {
      table.setParent( this );
      context = new LayoutContext<Table>( this, table );
      reconciliation = context.getReconciliation();
      super.setLayout( layoutFactory.create( context ) );
      table.addDisposeListener( this );
    }
  }

  @Override
  public void widgetDisposed( DisposeEvent e ) {
    if( !isDisposed() ) {
      dispose();
    }
  }

  @Override
  public void setLayout( Layout layout ) {
    throw new UnsupportedOperationException( "TreeAdapter does not allow to change its layout" );
  }

  @Override
  public ScrollBar getVerticalBar() {
    return layoutFactory.getVerticalBarAdapter();
  }

  @Override
  public ScrollBar getHorizontalBar() {
    return layoutFactory.getHorizontalBarAdapter();
  }

  @Override
  public void setSize( final int width, final int height ) {
    reconciliation.runWithSuspendedBoundsReconciliation( new Runnable() {
      @Override
      public void run() {
        TableAdapter.super.setSize( width, height );
      }
    } );
  }

  @Override
  public void setBounds( final int x, final int y, final int width, final int height ) {
    reconciliation.runWithSuspendedBoundsReconciliation( new Runnable() {
      @Override
      public void run() {
        TableAdapter.super.setBounds( x, y, width, height );
      }
    } );
  }

  @Override
  public void setLocation( final int x, final int y ) {
    reconciliation.runWithSuspendedBoundsReconciliation( new Runnable() {
      @Override
      public void run() {
        TableAdapter.super.setLocation( x, y );
      }
    } );
  }

  @Override
  public void setVisible( boolean visible ) {
    super.setVisible( reconciliation.setVisible( visible ) );
  }

  ////////////////////////////////////////////////////
  // scroll bar style attributes

  @Override
  public void setIncrementButtonLength( int length ) {
    layoutFactory.setIncrementButtonLength( length );
  }

  @Override
  public int getIncrementButtonLength() {
    return layoutFactory.getIncrementButtonLength();
  }

  @Override
  public void setIncrementColor( Color color ) {
    layoutFactory.setIncrementColor( color );
  }

  @Override
  public Color getIncrementColor() {
    return layoutFactory.getIncrementColor();
  }

  @Override
  public void setPageIncrementColor( Color color ) {
    layoutFactory.setPageIncrementColor( color );
  }

  @Override
  public Color getPageIncrementColor() {
    return layoutFactory.getPageIncrementColor();
  }

  @Override
  public void setThumbColor( Color color ) {
    layoutFactory.setThumbColor( color );
  }

  @Override
  public Color getThumbColor() {
    return layoutFactory.getThumbColor();
  }

  //////////////////////////////////////////////////////
  // delegating adapter methods


  @Override
  public Object getData() {
    return table.getData();
  }

  @Override
  public Object getData( String key ) {
    return table.getData( key );
  }

  @Override
  public void setBackgroundMode( int mode ) {
    table.setBackgroundMode( mode );
  }

  @Override
  public boolean setFocus() {
    return table.setFocus();
  }

  @Override
  public boolean forceFocus() {
    return table.forceFocus();
  }

  @Override
  public Color getBackground() {
    return table.getBackground();
  }

  @Override
  public void setData( Object data ) {
    table.setData( data );
  }

  @Override
  public Image getBackgroundImage() {
    return table.getBackgroundImage();
  }

  @Override
  public void setData( String key, Object value ) {
    table.setData( key, value );
  }

  @Override
  public void setTabList( Control[] tabList ) {
    table.setTabList( tabList );
  }

  @Override
  public boolean getEnabled() {
    return table.getEnabled();
  }

  @Override
  public Font getFont() {
    return table.getFont();
  }

  @Override
  public Color getForeground() {
    return table.getForeground();
  }

  @Override
  public String toString() {
    if( table != null ) {
      return table.toString();
    }
    return table.toString();
  }

  @Override
  public Control[] getTabList() {
    return table.getTabList();
  }

  @Override
  public boolean getVisible() {
    return table.getVisible();
  }

  @Override
  public boolean isFocusControl() {
    return table.isFocusControl();
  }

  @Override
  public boolean getHeaderVisible() {
    return table.getHeaderVisible();
  }

  @Override
  public void setBackground( Color color ) {
    table.setBackground( color );
  }

  @Override
  public boolean getLinesVisible() {
    return table.getLinesVisible();
  }

  @Override
  public void setBackgroundImage( Image image ) {
    table.setBackgroundImage( image );
  }

  @Override
  public void setForeground( Color color ) {
    table.setForeground( color );
  }

  @Override
  public boolean traverse( int traversal ) {
    return table.traverse( traversal );
  }

  @Override
  public boolean traverse( int traversal, Event event ) {
    return table.traverse( traversal, event );
  }

  @Override
  public boolean traverse( int traversal, KeyEvent event ) {
    return table.traverse( traversal, event );
  }

  @Override
  public void setFont( Font font ) {
    table.setFont( font );
  }

  @Override
  public void setHeaderVisible( boolean show ) {
    table.setHeaderVisible( show );
  }

  @Override
  public void setLinesVisible( boolean show ) {
    table.setLinesVisible( show );
  }

  ///////////////////////////////
  // private helper methods

  private static LayoutMapping<Table> createLayoutMapping() {
    return new LayoutMapping<Table>( new TableLayoutFactory(), WIN32 );
  }
}