package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType.GTK;
import static com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType.WIN32;
import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.createLayoutFactory;

import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.GestureListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TouchListener;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;

public class TableAdapter extends Table implements Adapter<Table>, DisposeListener, ScrollbarStyle {

  private LayoutFactory<Table> layoutFactory;
  private Table table;

  TableAdapter() {
    super( null, -1 );
  }

  @Override
  @SuppressWarnings("unchecked")
  public void adapt( Table table ) {
    this.layoutFactory = createLayoutFactory( new Platform(), createLayoutMapping() );
    this.table = table;
    table.setParent( this );
    super.setLayout( layoutFactory.create( table.getParent(), table ) );
    getParent().layout();
    table.addDisposeListener( this );
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
  public Rectangle computeTrim( int x, int y, int width, int height ) {
    return table.computeTrim( x, y, width, height );
  }

  @Override
  public void addControlListener( ControlListener listener ) {
    table.addControlListener( listener );
  }

  @Override
  public void addDragDetectListener( DragDetectListener listener ) {
    table.addDragDetectListener( listener );
  }

  @Override
  public void addListener( int eventType, Listener listener ) {
    table.addListener( eventType, listener );
  }

  @Override
  public void addFocusListener( FocusListener listener ) {
    table.addFocusListener( listener );
  }

  @Override
  public void addGestureListener( GestureListener listener ) {
    table.addGestureListener( listener );
  }

  @Override
  public void addDisposeListener( DisposeListener listener ) {
    table.addDisposeListener( listener );
  }

  @Override
  public void addHelpListener( HelpListener listener ) {
    table.addHelpListener( listener );
  }

  @Override
  public void addKeyListener( KeyListener listener ) {
    table.addKeyListener( listener );
  }

  @Override
  public void addMenuDetectListener( MenuDetectListener listener ) {
    table.addMenuDetectListener( listener );
  }

  @Override
  public void drawBackground( GC gc, int x, int y, int width, int height, int offsetX, int offsetY ) {
    table.drawBackground( gc, x, y, width, height, offsetX, offsetY );
  }

  @Override
  public void addMouseListener( MouseListener listener ) {
    table.addMouseListener( listener );
  }

  @Override
  public void addMouseTrackListener( MouseTrackListener listener ) {
    table.addMouseTrackListener( listener );
  }

  @Override
  public void addMouseMoveListener( MouseMoveListener listener ) {
    table.addMouseMoveListener( listener );
  }

  @Override
  public int getBackgroundMode() {
    return table.getBackgroundMode();
  }

  @Override
  public void addMouseWheelListener( MouseWheelListener listener ) {
    table.addMouseWheelListener( listener );
  }

  @Override
  public void addSelectionListener( SelectionListener listener ) {
    table.addSelectionListener( listener );
  }

  @Override
  public void addPaintListener( PaintListener listener ) {
    table.addPaintListener( listener );
  }

  @Override
  public Control[] getTabList() {
    return table.getTabList();
  }

  @Override
  public void addTouchListener( TouchListener listener ) {
    table.addTouchListener( listener );
  }

  @Override
  public void addTraverseListener( TraverseListener listener ) {
    table.addTraverseListener( listener );
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
  public Point computeSize( int wHint, int hHint ) {
    return table.computeSize( wHint, hHint );
  }

  @Override
  public Listener[] getListeners( int eventType ) {
    return table.getListeners( eventType );
  }

  @Override
  public int getStyle() {
    return table.getStyle();
  }

  @Override
  public boolean isListening( int eventType ) {
    return table.isListening( eventType );
  }

  @Override
  public void notifyListeners( int eventType, Event event ) {
    table.notifyListeners( eventType, event );
  }

  @Override
  public boolean dragDetect( Event event ) {
    return table.dragDetect( event );
  }

  @Override
  public boolean dragDetect( MouseEvent event ) {
    return table.dragDetect( event );
  }

  @Override
  public void removeListener( int eventType, Listener listener ) {
    table.removeListener( eventType, listener );
  }

  @Override
  public void removeDisposeListener( DisposeListener listener ) {
    table.removeDisposeListener( listener );
  }

  @Override
  public void reskin( int flags ) {
    table.reskin( flags );
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
  public void setTabList( Control[] tabList ) {
    table.setTabList( tabList );
  }

  @Override
  public Accessible getAccessible() {
    return table.getAccessible();
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
  public Cursor getCursor() {
    return table.getCursor();
  }

  @Override
  public boolean getDragDetect() {
    return table.getDragDetect();
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
  public void clear( int index ) {
    table.clear( index );
  }

  @Override
  public Color getForeground() {
    return table.getForeground();
  }

  @Override
  public Point getLocation() {
    return table.getLocation();
  }

  @Override
  public Menu getMenu() {
    return table.getMenu();
  }

  @Override
  public void clear( int start, int end ) {
    table.clear( start, end );
  }

  @Override
  public Monitor getMonitor() {
    return table.getMonitor();
  }

  @Override
  public int getOrientation() {
    return table.getOrientation();
  }

  @Override
  public String toString() {
    return table.toString();
  }

  @Override
  public void clear( int[] indices ) {
    table.clear( indices );
  }

  @Override
  public Region getRegion() {
    return table.getRegion();
  }

  @Override
  public int getTextDirection() {
    return table.getTextDirection();
  }

  @Override
  public void clearAll() {
    table.clearAll();
  }

  @Override
  public String getToolTipText() {
    return table.getToolTipText();
  }

  @Override
  public boolean getTouchEnabled() {
    return table.getTouchEnabled();
  }

  @Override
  public boolean getVisible() {
    return table.getVisible();
  }

  @Override
  public Point computeSize( int wHint, int hHint, boolean changed ) {
    return table.computeSize( wHint, hHint, changed );
  }

  @Override
  public boolean isFocusControl() {
    return table.isFocusControl();
  }

  @Override
  public boolean isReparentable() {
    return table.isReparentable();
  }

  @Override
  public boolean isVisible() {
    return table.isVisible();
  }

  @Override
  public void deselect( int[] indices ) {
    table.deselect( indices );
  }

  @Override
  public void deselect( int index ) {
    table.deselect( index );
  }

  @Override
  public void deselect( int start, int end ) {
    table.deselect( start, end );
  }

  @Override
  public void deselectAll() {
    table.deselectAll();
  }

  @Override
  public void removeControlListener( ControlListener listener ) {
    table.removeControlListener( listener );
  }

  @Override
  public void removeDragDetectListener( DragDetectListener listener ) {
    table.removeDragDetectListener( listener );
  }

  @Override
  public void removeFocusListener( FocusListener listener ) {
    table.removeFocusListener( listener );
  }

  @Override
  public void removeGestureListener( GestureListener listener ) {
    table.removeGestureListener( listener );
  }

  @Override
  public void removeHelpListener( HelpListener listener ) {
    table.removeHelpListener( listener );
  }

  @Override
  public void removeKeyListener( KeyListener listener ) {
    table.removeKeyListener( listener );
  }

  @Override
  public void removeMenuDetectListener( MenuDetectListener listener ) {
    table.removeMenuDetectListener( listener );
  }

  @Override
  public TableColumn getColumn( int index ) {
    return table.getColumn( index );
  }

  @Override
  public void removeMouseTrackListener( MouseTrackListener listener ) {
    table.removeMouseTrackListener( listener );
  }

  @Override
  public int getColumnCount() {
    return table.getColumnCount();
  }

  @Override
  public void removeMouseListener( MouseListener listener ) {
    table.removeMouseListener( listener );
  }

  @Override
  public int[] getColumnOrder() {
    return table.getColumnOrder();
  }

  @Override
  public void removeMouseMoveListener( MouseMoveListener listener ) {
    table.removeMouseMoveListener( listener );
  }

  @Override
  public void removeMouseWheelListener( MouseWheelListener listener ) {
    table.removeMouseWheelListener( listener );
  }

  @Override
  public TableColumn[] getColumns() {
    return table.getColumns();
  }

  @Override
  public void removePaintListener( PaintListener listener ) {
    table.removePaintListener( listener );
  }

  @Override
  public int getGridLineWidth() {
    return table.getGridLineWidth();
  }

  @Override
  public void removeTouchListener( TouchListener listener ) {
    table.removeTouchListener( listener );
  }

  @Override
  public int getHeaderHeight() {
    return table.getHeaderHeight();
  }

  @Override
  public boolean getHeaderVisible() {
    return table.getHeaderVisible();
  }

  @Override
  public void removeTraverseListener( TraverseListener listener ) {
    table.removeTraverseListener( listener );
  }

  @Override
  public TableItem getItem( int index ) {
    return table.getItem( index );
  }

  @Override
  public TableItem getItem( Point point ) {
    return table.getItem( point );
  }

  @Override
  public int getItemCount() {
    return table.getItemCount();
  }

  @Override
  public int getItemHeight() {
    return table.getItemHeight();
  }

  @Override
  public TableItem[] getItems() {
    return table.getItems();
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
  public TableItem[] getSelection() {
    return table.getSelection();
  }

  @Override
  public int getSelectionCount() {
    return table.getSelectionCount();
  }

  @Override
  public int getSelectionIndex() {
    return table.getSelectionIndex();
  }

  @Override
  public int[] getSelectionIndices() {
    return table.getSelectionIndices();
  }

  @Override
  public TableColumn getSortColumn() {
    return table.getSortColumn();
  }

  @Override
  public void setCapture( boolean capture ) {
    table.setCapture( capture );
  }

  @Override
  public int getSortDirection() {
    return table.getSortDirection();
  }

  @Override
  public int getTopIndex() {
    return table.getTopIndex();
  }

  @Override
  public void setCursor( Cursor cursor ) {
    table.setCursor( cursor );
  }

  @Override
  public void setDragDetect( boolean dragDetect ) {
    table.setDragDetect( dragDetect );
  }

  @Override
  public int indexOf( TableColumn column ) {
    return table.indexOf( column );
  }

  @Override
  public void setForeground( Color color ) {
    table.setForeground( color );
  }

  @Override
  public int indexOf( TableItem item ) {
    return table.indexOf( item );
  }

  @Override
  public boolean isSelected( int index ) {
    return table.isSelected( index );
  }

  @Override
  public void setMenu( Menu menu ) {
    table.setMenu( menu );
  }

  @Override
  public void setOrientation( int orientation ) {
    table.setOrientation( orientation );
  }

  @Override
  public void remove( int[] indices ) {
    table.remove( indices );
  }

  @Override
  public void remove( int index ) {
    table.remove( index );
  }

  @Override
  public void setRegion( Region region ) {
    table.setRegion( region );
  }

  @Override
  public void remove( int start, int end ) {
    table.remove( start, end );
  }

  @Override
  public void removeAll() {
    table.removeAll();
  }

  @Override
  public void setTextDirection( int textDirection ) {
    table.setTextDirection( textDirection );
  }

  @Override
  public void setToolTipText( String string ) {
    table.setToolTipText( string );
  }

  @Override
  public void removeSelectionListener( SelectionListener listener ) {
    table.removeSelectionListener( listener );
  }

  @Override
  public void setTouchEnabled( boolean enabled ) {
    table.setTouchEnabled( enabled );
  }

  @Override
  public void select( int[] indices ) {
    table.select( indices );
  }

  @Override
  public void setVisible( boolean visible ) {
    table.setVisible( visible );
  }

  @Override
  public void select( int index ) {
    table.select( index );
  }

  @Override
  public void select( int start, int end ) {
    table.select( start, end );
  }

  @Override
  public void selectAll() {
    table.selectAll();
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
  public void setColumnOrder( int[] order ) {
    table.setColumnOrder( order );
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
  public void setItemCount( int count ) {
    table.setItemCount( count );
  }

  @Override
  public void setLinesVisible( boolean show ) {
    table.setLinesVisible( show );
  }

  @Override
  public void setSelection( int[] indices ) {
    table.setSelection( indices );
  }

  @Override
  public void setSelection( TableItem item ) {
    table.setSelection( item );
  }

  @Override
  public void setSelection( TableItem[] items ) {
    table.setSelection( items );
  }

  @Override
  public void setSelection( int index ) {
    table.setSelection( index );
  }

  @Override
  public void setSelection( int start, int end ) {
    table.setSelection( start, end );
  }

  @Override
  public void setSortColumn( TableColumn column ) {
    table.setSortColumn( column );
  }

  @Override
  public void setSortDirection( int direction ) {
    table.setSortDirection( direction );
  }

  @Override
  public void setTopIndex( int index ) {
    table.setTopIndex( index );
  }

  @Override
  public void showColumn( TableColumn column ) {
    table.showColumn( column );
  }

  @Override
  public void showItem( TableItem item ) {
    table.showItem( item );
  }

  @Override
  public void showSelection() {
    table.showSelection();
  }

  ///////////////////////////////
  // private helper methods

  private static LayoutMapping<Table> createLayoutMapping() {
    return new LayoutMapping<Table>( new TableLayoutFactory(), WIN32, GTK );
  }
}