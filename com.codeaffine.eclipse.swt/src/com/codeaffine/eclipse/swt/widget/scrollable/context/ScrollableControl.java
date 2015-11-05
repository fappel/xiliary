package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static com.codeaffine.eclipse.swt.util.ArgumentVerification.verifyNotNull;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

public class ScrollableControl <T extends Scrollable> {

  private final T scrollable;

  public ScrollableControl( T scrollable ) {
    this.scrollable = scrollable;
  }

  public int getItemHeight() {
    if( scrollable instanceof Table ) {
      return ( ( Table )scrollable ).getItemHeight();
    }
    if( scrollable instanceof Tree ) {
      return ( ( Tree )scrollable ).getItemHeight();
    }
    throw new IllegalArgumentException( "Unsupported scrollable type: " + scrollable.getClass().getName() );
  }

  public boolean isOwnerDrawn() {
    return scrollable.getListeners( SWT.MeasureItem ).length != 0;
  }

  public boolean isSameAs( Widget other ) {
    return scrollable == other;
  }

  public void addListener( int eventType, Listener listener ) {
    scrollable.addListener( eventType, listener );
  }

  public Display getDisplay() {
    return scrollable.getDisplay();
  }

  public void setRedraw( boolean redraw ) {
    scrollable.setRedraw( redraw );
  }

  public void setParent( Composite parent ) {
    scrollable.setParent( parent );
  }

  public boolean isChildOf( Composite parent ) {
    return scrollable.getParent() == parent;
  }

  public Point computePreferredSize() {
    return computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
  }

  public Point computeSize( int wHint, int hHint, boolean flushCache ) {
    return scrollable.computeSize( wHint, hHint, flushCache );
  }

  public Point getSize() {
    return scrollable.getSize();
  }

  public void setSize( int width, int height ) {
    scrollable.setSize( new Point( width, height ) );
  }

  public void setSize( Point size ) {
    scrollable.setSize( size );
  }

  public Rectangle getBounds() {
    return scrollable.getBounds();
  }

  public Point getLocation() {
    return scrollable.getLocation();
  }

  public void setLocation( Point location ) {
    scrollable.setLocation( location );
  }

  public int getBorderWidth() {
    return scrollable.getBorderWidth();
  }

  public Color getBackground() {
    return scrollable.getBackground();
  }

  public void setData( String key, Object value ) {
    scrollable.setData( key, value );
  }

  public Object getData( String key ) {
    return scrollable.getData( key );
  }

  public boolean hasStyle( int style ) {
    return ( scrollable.getStyle() & style ) != 0;
  }

  public void setVisible( boolean visible ) {
    scrollable.setVisible( visible );
  }

  public boolean getVisible() {
    return scrollable.getVisible();
  }

  public boolean isInstanceof( Class<? extends Scrollable> type ) {
    verifyNotNull( type, "type" );

    return type.isInstance( scrollable );
  }

  public T getControl() {
    return scrollable;
  }

  /////////////////////////////////////
  // scrollbar properties

  public boolean isVerticalBarVisible() {
    return scrollable.getVerticalBar().isVisible();
  }

  public Point getVerticalBarSize() {
    return scrollable.getVerticalBar().getSize();
  }

  public boolean isHorizontalBarVisible() {
    return scrollable.getHorizontalBar().isVisible();
  }

  public Point getHorizontalBarSize() {
    return scrollable.getHorizontalBar().getSize();
  }

  public void setHorizontalBarVisible( boolean visible ) {
    scrollable.getHorizontalBar().setVisible( visible );
  }

  public int getHorizontalBarMaximum() {
    return scrollable.getHorizontalBar().getMaximum();
  }
}