package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.createLayoutFactory;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.codeaffine.eclipse.swt.util.Platform;
import com.codeaffine.eclipse.swt.util.PlatformSupport;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.Reconciliation;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

public class TableAdapter extends Table implements Adapter<Table>, DisposeListener, ScrollbarStyle {

  private LayoutFactory<Table> layoutFactory;
  private Reconciliation reconciliation;
  private AdaptionContext<Table> context;
  private Table table;

  TableAdapter() {
    super( null, -1 );
  }

  @Override
  public Table getScrollable() {
    return table;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void adapt( Table table, PlatformSupport platformSupport ) {
    this.layoutFactory = createLayoutFactory( new Platform(), createLayoutMapping( platformSupport ) );
    this.table = table;
    if( platformSupport.isGranted() ) {
      initialize();
    }
  }

  ///////////////////////////////
  // Table overrides

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
  public void setSize( int width, int height ) {
    reconciliation.runWithSuspendedBoundsReconciliation( () -> super.setSize( width, height ) );
  }

  @Override
  public void setBounds( int x, int y, int width, int height ) {
    reconciliation.runWithSuspendedBoundsReconciliation( () ->  super.setBounds( x, y, width, height ) );
  }

  @Override
  public void setLocation( int x, int y ) {
    reconciliation.runWithSuspendedBoundsReconciliation( () -> super.setLocation( x, y ) );
  }

  @Override
  public void setVisible( boolean visible ) {
    super.setVisible( reconciliation.setVisible( visible ) );
  }

  @Override
  public void setEnabled( boolean enabled ) {
    super.setEnabled( reconciliation.setEnabled( enabled ) );
  }

  @Override
  public TableColumn getColumn( int index ) {
    return table.getColumn( index );
  }

  @Override
  public int getColumnCount() {
    return table.getColumnCount();
  }

  @Override
  public int[] getColumnOrder() {
    return table.getColumnOrder();
  }

  @Override
  public TableColumn[] getColumns() {
    return table == null ? new TableColumn[ 0 ] : table.getColumns();
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

  @Override
  public void setBackgroundColor( Color color ) {
    layoutFactory.setBackgroundColor( color );
  }

  @Override
  public Color getBackgroundColor() {
    return layoutFactory.getBackgroundColor();
  }

  //////////////////////////////////////////////////////
  // delegating adapter methods

  @Override
  public Point computeSize( int wHint, int hHint, boolean changed ) {
   return table.computeSize( wHint, hHint, changed );
  }

  @Override
  public Object getLayoutData() {
    return table.getLayoutData();
  }

  @Override
  public void setLayoutData( Object layoutData ) {
    table.setLayoutData( layoutData );
  }

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

  private void initialize() {
    table.setParent( this );
    ScrollableControl<Table> scrollableControl = new ScrollableControl<>( table );
    new ItemHeightMeasurementEnabler( scrollableControl, this );
    context = new AdaptionContext<>( this, scrollableControl );
    reconciliation = context.getReconciliation();
    super.setLayout( layoutFactory.create( context ) );
    table.addDisposeListener( this );
  }

  private static LayoutMapping<Table> createLayoutMapping( PlatformSupport platformSupport ) {
    return new LayoutMapping<Table>( new TableLayoutFactory(), platformSupport.getSupportedTypes() );
  }
}