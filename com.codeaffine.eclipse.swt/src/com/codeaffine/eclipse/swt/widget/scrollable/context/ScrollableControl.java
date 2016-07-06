/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static com.codeaffine.util.ArgumentVerification.verifyNotNull;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.valueOf;
import static java.lang.Integer.valueOf;

import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

public class ScrollableControl <T extends Scrollable> {

  static final Point POINT_OF_ORIGIN = new Point( 0, 0 );
  static final Integer ZERO = valueOf( 0 );

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
    if( scrollable instanceof StyledText ) {
      return ( ( StyledText )scrollable ).getLineHeight();
    }
    return ZERO.intValue();
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

  public void removeListener( int eventType, Listener listener ) {
    scrollable.removeListener( eventType, listener );
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
    if( hasStyle( SWT.BORDER ) ) {
      return scrollable.getBorderWidth();
    }
    return 0;
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

  public boolean getEnabled() {
    return scrollable.getEnabled();
  }

  public void setEnabled( boolean enabled ) {
    scrollable.setEnabled( enabled );
  }

  public boolean isInstanceof( Class<? extends Scrollable> type ) {
    verifyNotNull( type, "type" );

    return type.isInstance( scrollable );
  }

  public boolean isStructuredScrollableType() {
    return isInstanceof( Table.class ) || isInstanceof( Tree.class );
  }

  public T getControl() {
    return scrollable;
  }

  public boolean isVerticalBarVisible() {
    return getSafely( scrollable.getVerticalBar(), bar -> valueOf( bar.isVisible() ), FALSE ).booleanValue();
  }

  public Point getVerticalBarSize() {
    return getSafely( scrollable.getVerticalBar(), bar -> bar.getSize(), POINT_OF_ORIGIN );
  }

  public int getVerticalBarSelection() {
    return getSafely( scrollable.getVerticalBar(), bar -> valueOf( bar.getSelection() ), ZERO ).intValue();
  }

  public int getVerticalBarThumb() {
    return getSafely( scrollable.getVerticalBar(), bar -> valueOf( bar.getThumb() ), ZERO ).intValue();
  }

  public int getVerticalBarPageIncrement() {
    return getSafely( scrollable.getVerticalBar(), bar -> valueOf( bar.getPageIncrement() ), ZERO ).intValue();
  }

  public int getVerticalBarMaximum() {
    return getSafely( scrollable.getVerticalBar(), bar -> valueOf( bar.getMaximum() ), ZERO ).intValue();
  }

  public int getVerticalBarIncrement() {
    return getSafely( scrollable.getVerticalBar(), bar -> valueOf( bar.getIncrement() ), ZERO ).intValue();
  }

  public boolean isHorizontalBarVisible() {
    return getSafely( scrollable.getHorizontalBar(), bar -> valueOf( bar.isVisible() ), FALSE ).booleanValue();
  }

  public Point getHorizontalBarSize() {
    return getSafely( scrollable.getHorizontalBar(), bar -> bar.getSize(), POINT_OF_ORIGIN );
  }

  public void setHorizontalBarVisible( boolean visible ) {
    setSafely( scrollable.getHorizontalBar(), bar -> bar.setVisible( visible ) );
  }

  public boolean getHorizontalBarVisible() {
    return getSafely( scrollable.getHorizontalBar(), bar -> valueOf( bar.getVisible() ), FALSE ).booleanValue();
  }

  public int getHorizontalBarMaximum() {
    return getSafely( scrollable.getHorizontalBar(), bar -> valueOf( bar.getMaximum() ), ZERO ).intValue();
  }

  public boolean hasHorizontalBar() {
    return exists( scrollable.getHorizontalBar() );
  }

  private static void setSafely( ScrollBar scrollbar, Consumer<ScrollBar> consumer ) {
    if( exists( scrollbar ) ) {
      consumer.accept( scrollbar );
    }
  }

  private static <R> R getSafely( ScrollBar scrollbar, Function<ScrollBar,R> function, R defaultResult ) {
    if( !exists( scrollbar ) ) {
      return defaultResult;
    }
    return function.apply( scrollbar );
  }

  private static boolean exists( ScrollBar scrollbar ) {
    return scrollbar != null;
  }
}