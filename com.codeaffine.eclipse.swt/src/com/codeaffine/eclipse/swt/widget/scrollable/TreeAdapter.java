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
import org.eclipse.swt.events.TreeListener;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;

public class TreeAdapter extends Tree implements Adapter<Tree>, DisposeListener, ScrollbarStyle {

  private LayoutFactory<Tree> layoutFactory;
  private Tree tree;

  TreeAdapter() {
    super( null, -1 );
  }

  @Override
  @SuppressWarnings("unchecked")
  public void adapt( Tree tree ) {
    this.layoutFactory = createLayoutFactory( new Platform(), createLayoutMapping() );
    this.tree = tree;
    tree.setParent( this );
    super.setLayout( layoutFactory.create( tree.getParent(), tree ) );
    getParent().layout();
    tree.addDisposeListener( this );
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
    return tree.computeTrim( x, y, width, height );
  }

  @Override
  public void addControlListener( ControlListener listener ) {
    tree.addControlListener( listener );
  }

  @Override
  public void addDragDetectListener( DragDetectListener listener ) {
    tree.addDragDetectListener( listener );
  }

  @Override
  public void addListener( int eventType, Listener listener ) {
    tree.addListener( eventType, listener );
  }

  @Override
  public void addFocusListener( FocusListener listener ) {
    tree.addFocusListener( listener );
  }

  @Override
  public void addGestureListener( GestureListener listener ) {
    tree.addGestureListener( listener );
  }

  @Override
  public void addDisposeListener( DisposeListener listener ) {
    tree.addDisposeListener( listener );
  }

  @Override
  public void addHelpListener( HelpListener listener ) {
    tree.addHelpListener( listener );
  }

  @Override
  public void addKeyListener( KeyListener listener ) {
    tree.addKeyListener( listener );
  }

  @Override
  public void addMenuDetectListener( MenuDetectListener listener ) {
    tree.addMenuDetectListener( listener );
  }

  @Override
  public void drawBackground( GC gc, int x, int y, int width, int height, int offsetX, int offsetY ) {
    tree.drawBackground( gc, x, y, width, height, offsetX, offsetY );
  }

  @Override
  public void addMouseListener( MouseListener listener ) {
    tree.addMouseListener( listener );
  }

  @Override
  public void addSelectionListener( SelectionListener listener ) {
    tree.addSelectionListener( listener );
  }

  @Override
  public void addMouseTrackListener( MouseTrackListener listener ) {
    tree.addMouseTrackListener( listener );
  }

  @Override
  public void addTreeListener( TreeListener listener ) {
    tree.addTreeListener( listener );
  }

  @Override
  public void addMouseMoveListener( MouseMoveListener listener ) {
    tree.addMouseMoveListener( listener );
  }

  @Override
  public int getBackgroundMode() {
    return tree.getBackgroundMode();
  }

  @Override
  public void addMouseWheelListener( MouseWheelListener listener ) {
    tree.addMouseWheelListener( listener );
  }

  @Override
  public Control[] getChildren() {
    return tree.getChildren();
  }

  @Override
  public void addPaintListener( PaintListener listener ) {
    tree.addPaintListener( listener );
  }

  @Override
  public Control[] getTabList() {
    return tree.getTabList();
  }

  @Override
  public void addTouchListener( TouchListener listener ) {
    tree.addTouchListener( listener );
  }

  @Override
  public void addTraverseListener( TraverseListener listener ) {
    tree.addTraverseListener( listener );
  }

  @Override
  public Object getData() {
    return tree.getData();
  }

  @Override
  public Object getData( String key ) {
    return tree.getData( key );
  }

  @Override
  public Point computeSize( int wHint, int hHint ) {
    return tree.computeSize( wHint, hHint );
  }

  @Override
  public Listener[] getListeners( int eventType ) {
    return tree.getListeners( eventType );
  }

  @Override
  public int getStyle() {
    return tree.getStyle();
  }

  @Override
  public boolean isListening( int eventType ) {
    return tree.isListening( eventType );
  }

  @Override
  public void notifyListeners( int eventType, Event event ) {
    tree.notifyListeners( eventType, event );
  }

  @Override
  public boolean dragDetect( Event event ) {
    return tree.dragDetect( event );
  }

  @Override
  public boolean dragDetect( MouseEvent event ) {
    return tree.dragDetect( event );
  }

  @Override
  public void removeListener( int eventType, Listener listener ) {
    tree.removeListener( eventType, listener );
  }

  @Override
  public void removeDisposeListener( DisposeListener listener ) {
    tree.removeDisposeListener( listener );
  }

  @Override
  public void reskin( int flags ) {
    tree.reskin( flags );
  }

  @Override
  public void setBackgroundMode( int mode ) {
    tree.setBackgroundMode( mode );
  }

  @Override
  public boolean setFocus() {
    return tree.setFocus();
  }

  @Override
  public boolean forceFocus() {
    return tree.forceFocus();
  }

  @Override
  public void setTabList( Control[] tabList ) {
    tree.setTabList( tabList );
  }

  @Override
  public Accessible getAccessible() {
    return tree.getAccessible();
  }

  @Override
  public Color getBackground() {
    return tree.getBackground();
  }

  @Override
  public void setData( Object data ) {
    tree.setData( data );
  }

  @Override
  public Image getBackgroundImage() {
    return tree.getBackgroundImage();
  }

  @Override
  public void setData( String key, Object value ) {
    tree.setData( key, value );
  }

  @Override
  public Cursor getCursor() {
    return tree.getCursor();
  }

  @Override
  public boolean getDragDetect() {
    return tree.getDragDetect();
  }

  @Override
  public boolean getEnabled() {
    return tree.getEnabled();
  }

  @Override
  public Font getFont() {
    return tree.getFont();
  }

  @Override
  public Color getForeground() {
    return tree.getForeground();
  }

  @Override
  public Menu getMenu() {
    return tree.getMenu();
  }

  @Override
  public Monitor getMonitor() {
    return tree.getMonitor();
  }

  @Override
  public int getOrientation() {
    return tree.getOrientation();
  }

  @Override
  public String toString() {
    if( tree != null ) {
      return tree.toString();
    }
    return super.toString();
  }

  @Override
  public Region getRegion() {
    return tree.getRegion();
  }

  @Override
  public int getTextDirection() {
    return tree.getTextDirection();
  }

  @Override
  public String getToolTipText() {
    return tree.getToolTipText();
  }

  @Override
  public boolean getTouchEnabled() {
    return tree.getTouchEnabled();
  }

  @Override
  public boolean getVisible() {
    return tree.getVisible();
  }

  @Override
  public boolean isFocusControl() {
    return tree.isFocusControl();
  }

  @Override
  public boolean isReparentable() {
    return tree.isReparentable();
  }

  @Override
  public boolean isVisible() {
    return tree.isVisible();
  }

  @Override
  public void clear( int index, boolean all ) {
    tree.clear( index, all );
  }

  @Override
  public void clearAll( boolean all ) {
    tree.clearAll( all );
  }

  @Override
  public Point computeSize( int wHint, int hHint, boolean changed ) {
    return tree.computeSize( wHint, hHint, changed );
  }

  @Override
  public void removeControlListener( ControlListener listener ) {
    tree.removeControlListener( listener );
  }

  @Override
  public void removeDragDetectListener( DragDetectListener listener ) {
    tree.removeDragDetectListener( listener );
  }

  @Override
  public void removeFocusListener( FocusListener listener ) {
    tree.removeFocusListener( listener );
  }

  @Override
  public void removeGestureListener( GestureListener listener ) {
    tree.removeGestureListener( listener );
  }

  @Override
  public void removeHelpListener( HelpListener listener ) {
    tree.removeHelpListener( listener );
  }

  @Override
  public void removeKeyListener( KeyListener listener ) {
    tree.removeKeyListener( listener );
  }

  @Override
  public void removeMenuDetectListener( MenuDetectListener listener ) {
    tree.removeMenuDetectListener( listener );
  }

  @Override
  public void removeMouseTrackListener( MouseTrackListener listener ) {
    tree.removeMouseTrackListener( listener );
  }

  @Override
  public void removeMouseListener( MouseListener listener ) {
    tree.removeMouseListener( listener );
  }

  @Override
  public void deselect( TreeItem item ) {
    tree.deselect( item );
  }

  @Override
  public void removeMouseMoveListener( MouseMoveListener listener ) {
    tree.removeMouseMoveListener( listener );
  }

  @Override
  public void deselectAll() {
    tree.deselectAll();
  }

  @Override
  public void removeMouseWheelListener( MouseWheelListener listener ) {
    tree.removeMouseWheelListener( listener );
  }

  @Override
  public void removePaintListener( PaintListener listener ) {
    tree.removePaintListener( listener );
  }

  @Override
  public void removeTouchListener( TouchListener listener ) {
    tree.removeTouchListener( listener );
  }

  @Override
  public void removeTraverseListener( TraverseListener listener ) {
    tree.removeTraverseListener( listener );
  }

  @Override
  public void setBackground( Color color ) {
    super.setBackground( color );
    tree.setBackground( color );
  }

  @Override
  public void setBackgroundImage( Image image ) {
    tree.setBackgroundImage( image );
  }

  @Override
  public void setCapture( boolean capture ) {
    tree.setCapture( capture );
  }

  @Override
  public void setCursor( Cursor cursor ) {
    tree.setCursor( cursor );
  }

  @Override
  public int getGridLineWidth() {
    return tree.getGridLineWidth();
  }

  @Override
  public int getHeaderHeight() {
    return tree.getHeaderHeight();
  }

  @Override
  public void setDragDetect( boolean dragDetect ) {
    tree.setDragDetect( dragDetect );
  }

  @Override
  public boolean getHeaderVisible() {
    return tree.getHeaderVisible();
  }

  @Override
  public TreeColumn getColumn( int index ) {
    return tree.getColumn( index );
  }

  @Override
  public void setForeground( Color color ) {
    tree.setForeground( color );
  }

  @Override
  public int getColumnCount() {
    return tree.getColumnCount();
  }

  @Override
  public int[] getColumnOrder() {
    return tree.getColumnOrder();
  }

  @Override
  public TreeColumn[] getColumns() {
    return tree.getColumns();
  }

  @Override
  public TreeItem getItem( int index ) {
    return tree.getItem( index );
  }

  @Override
  public void setMenu( Menu menu ) {
    tree.setMenu( menu );
  }

  @Override
  public void setOrientation( int orientation ) {
    tree.setOrientation( orientation );
  }

  @Override
  public TreeItem getItem( Point point ) {
    return tree.getItem( point );
  }

  @Override
  public int getItemCount() {
    return tree.getItemCount();
  }

  @Override
  public int getItemHeight() {
    return tree.getItemHeight();
  }

  @Override
  public void setRegion( Region region ) {
    tree.setRegion( region );
  }

  @Override
  public TreeItem[] getItems() {
    return tree.getItems();
  }

  @Override
  public boolean getLinesVisible() {
    return tree.getLinesVisible();
  }

  @Override
  public void setTextDirection( int textDirection ) {
    tree.setTextDirection( textDirection );
  }

  @Override
  public TreeItem getParentItem() {
    return tree.getParentItem();
  }

  @Override
  public void setToolTipText( String string ) {
    tree.setToolTipText( string );
  }

  @Override
  public void setTouchEnabled( boolean enabled ) {
    tree.setTouchEnabled( enabled );
  }

  @Override
  public void setVisible( boolean visible ) {
    tree.setVisible( visible );
  }

  @Override
  public TreeItem[] getSelection() {
    return tree.getSelection();
  }

  @Override
  public int getSelectionCount() {
    return tree.getSelectionCount();
  }

  @Override
  public TreeColumn getSortColumn() {
    return tree.getSortColumn();
  }

  @Override
  public int getSortDirection() {
    return tree.getSortDirection();
  }

  @Override
  public TreeItem getTopItem() {
    return tree.getTopItem();
  }

  @Override
  public int indexOf( TreeColumn column ) {
    return tree.indexOf( column );
  }

  @Override
  public boolean traverse( int traversal ) {
    return tree.traverse( traversal );
  }

  @Override
  public int indexOf( TreeItem item ) {
    return tree.indexOf( item );
  }

  @Override
  public boolean traverse( int traversal, Event event ) {
    return tree.traverse( traversal, event );
  }

  @Override
  public boolean traverse( int traversal, KeyEvent event ) {
    return tree.traverse( traversal, event );
  }

  @Override
  public void removeAll() {
    tree.removeAll();
  }

  @Override
  public void removeSelectionListener( SelectionListener listener ) {
    tree.removeSelectionListener( listener );
  }

  @Override
  public void removeTreeListener( TreeListener listener ) {
    tree.removeTreeListener( listener );
  }

  @Override
  public void setInsertMark( TreeItem item, boolean before ) {
    tree.setInsertMark( item, before );
  }

  @Override
  public void setItemCount( int count ) {
    tree.setItemCount( count );
  }

  @Override
  public void setLinesVisible( boolean show ) {
    tree.setLinesVisible( show );
  }

  @Override
  public void select( TreeItem item ) {
    tree.select( item );
  }

  @Override
  public void selectAll() {
    tree.selectAll();
  }

  @Override
  public void setColumnOrder( int[] order ) {
    tree.setColumnOrder( order );
  }

  @Override
  public void setFont( Font font ) {
    tree.setFont( font );
  }

  @Override
  public void setHeaderVisible( boolean show ) {
    tree.setHeaderVisible( show );
  }

  @Override
  public void setSelection( TreeItem item ) {
    tree.setSelection( item );
  }

  @Override
  public void setSelection( TreeItem[] items ) {
    tree.setSelection( items );
  }

  @Override
  public void setSortColumn( TreeColumn column ) {
    tree.setSortColumn( column );
  }

  @Override
  public void setSortDirection( int direction ) {
    tree.setSortDirection( direction );
  }

  @Override
  public void setTopItem( TreeItem item ) {
    tree.setTopItem( item );
  }

  @Override
  public void showColumn( TreeColumn column ) {
    tree.showColumn( column );
  }

  @Override
  public void showItem( TreeItem item ) {
    tree.showItem( item );
  }

  @Override
  public void showSelection() {
    tree.showSelection();
  }

  ///////////////////////////////
  // private helper methods

  private static LayoutMapping<Tree> createLayoutMapping() {
    return new LayoutMapping<Tree>( new TreeLayoutFactory(), WIN32, GTK );
  }
}