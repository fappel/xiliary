package com.codeaffine.eclipse.swt.widget.scrollable;

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
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;

public class TreeAdapter extends Tree implements Adapter<Tree>, DisposeListener, ScrollbarStyle {

  private LayoutFactory<Tree> layoutFactory;
  private Reconciliation reconciliation;
  private LayoutContext<Tree> context;
  private Tree tree;

  TreeAdapter() {
    super( null, -1 );
  }

  @Override
  @SuppressWarnings("unchecked")
  public void adapt( Tree tree, PlatformSupport platformSupport ) {
    this.layoutFactory = createLayoutFactory( new Platform(), createLayoutMapping( platformSupport ) );
    this.tree = tree;
    if( platformSupport.isGranted() ) {
      initialize();
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
        TreeAdapter.super.setSize( width, height );
      }
    } );
  }

  @Override
  public void setBounds( final int x, final int y, final int width, final int height ) {
    reconciliation.runWithSuspendedBoundsReconciliation( new Runnable() {
      @Override
      public void run() {
        TreeAdapter.super.setBounds( x, y, width, height );
      }
    } );
  }

  @Override
  public void setLocation( final int x, final int y ) {
    reconciliation.runWithSuspendedBoundsReconciliation( new Runnable() {
      @Override
      public void run() {
        TreeAdapter.super.setLocation( x, y );
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
  public int getBackgroundMode() {
    return super.getBackgroundMode();
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
  public void setBackgroundMode( int mode ) {
    super.setBackgroundMode( mode );
  }

  @Override
  public boolean setFocus() {
    return tree.setFocus();
  }

  @Override
  public void setTabList( Control[] tabList ) {
    tree.setTabList( tabList );
  }

  @Override
  public boolean forceFocus() {
    return tree.forceFocus();
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
  public boolean getLinesVisible() {
    return super.getLinesVisible();
  }

  @Override
  public String toString() {
    if( tree != null ) {
      return tree.toString();
    }
    return super.toString();
  }

  @Override
  public Control[] getTabList() {
    return tree.getTabList();
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
  public void setBackground( Color color ) {
    super.setBackground( color );
    tree.setBackground( color );
  }

  @Override
  public void setBackgroundImage( Image image ) {
    tree.setBackgroundImage( image );
  }

  @Override
  public boolean getHeaderVisible() {
    return tree.getHeaderVisible();
  }

  @Override
  public void setForeground( Color color ) {
    tree.setForeground( color );
  }

  @Override
  public boolean traverse( int traversal ) {
    return tree.traverse( traversal );
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
  public void setLinesVisible( boolean show ) {
    tree.setLinesVisible( show );
  }

  @Override
  public void setFont( Font font ) {
    tree.setFont( font );
  }

  @Override
  public void setHeaderVisible( boolean show ) {
    tree.setHeaderVisible( show );
  }

  ///////////////////////////////
  // private helper methods

  private void initialize() {
    tree.setParent( this );
    context = new LayoutContext<Tree>( this, tree );
    reconciliation = context.getReconciliation();
    super.setLayout( layoutFactory.create( context ) );
    tree.addDisposeListener( this );
  }

  private static LayoutMapping<Tree> createLayoutMapping( PlatformSupport platformSupport ) {
    return new LayoutMapping<Tree>( new TreeLayoutFactory(), platformSupport.getSupportedTypes() );
  }
}