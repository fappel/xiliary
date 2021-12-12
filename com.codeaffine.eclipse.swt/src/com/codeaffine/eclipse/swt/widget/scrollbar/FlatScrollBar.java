/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.widget.scrollbar.UntypedSelectionAdapter.lookup;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;

public class FlatScrollBar extends Composite {

  public static final int BAR_BREADTH = 6;

  static final int DEFAULT_MINIMUM = 0;
  static final int DEFAULT_MAXIMUM = 100;
  static final int DEFAULT_INCREMENT = 1;
  static final int DEFAULT_THUMB = 10;
  static final int DEFAULT_PAGE_INCREMENT = DEFAULT_THUMB;
  static final int DEFAULT_SELECTION = 0;
  static final int DEFAULT_BUTTON_LENGTH = 0;
  static final int DEFAULT_MAX_EXPANSION = Direction.CLEARANCE + 2;

  final ClickControl up;
  final ClickControl upFast;
  final DragControl drag;
  final ClickControl downFast;
  final ClickControl down;
  final Direction direction;
  final MouseWheelShifter mouseWheelHandler;
  final Collection<SelectionListener> listeners;

  private int minimum;
  private int maximum;
  private int increment;
  private int pageIncrement;
  private int thumb;
  private int selection;
  private boolean onDrag;
  private int buttonLength;

  public FlatScrollBar( final Composite parent, int style ) {
    this( parent, style, DEFAULT_BUTTON_LENGTH, DEFAULT_MAX_EXPANSION );
  }

  FlatScrollBar( Composite parent, int style, int buttonLength, int maxExpansion ) {
    super( parent, SWT.NONE );
    super.setLayout( new FlatScrollBarLayout( getDirection( style ) ) );
    this.minimum = DEFAULT_MINIMUM;
    this.maximum = DEFAULT_MAXIMUM;
    this.increment = DEFAULT_INCREMENT;
    this.pageIncrement = DEFAULT_PAGE_INCREMENT;
    this.thumb = DEFAULT_THUMB;
    this.selection = DEFAULT_SELECTION;
    this.buttonLength = buttonLength;
    this.direction = getDirection( style );
    this.direction.setDefaultSize( this );
    this.up = new ClickControl( this, new Decrementer( this ), maxExpansion );
    this.upFast = new ClickControl( this, new FastDecrementer( this ), maxExpansion );
    this.drag = new DragControl( this, new DragShifter( this, buttonLength ), maxExpansion );
    this.downFast = new ClickControl( this, new FastIncrementer( this ), maxExpansion );
    this.down = new ClickControl( this, new Incrementer( this ), maxExpansion );
    this.mouseWheelHandler = new MouseWheelShifter( this, parent, buttonLength );
    this.listeners = new HashSet<SelectionListener>();
    addMouseTrackListener( new MouseTracker( this, maxExpansion ) );
    addControlListener( new ResizeObserver( this ) );
    setDefaultColorScheme();
  }

  @Override
  public void setLayout( Layout layout ) {
    throw new UnsupportedOperationException( FlatScrollBar.class.getName() + " does not allow to change layout." );
  };

  @Override
  public int getStyle() {
    return direction != null ? super.getStyle() | direction.value() : super.getStyle();
  };

  Direction getDirection() {
    return direction;
  }

  public void setMinimum( int minimum ) {
    if( this.minimum != minimum && minimum >= 0 && minimum < maximum ) {
      this.minimum = minimum;
      adjustThumb();
      adjustSelection();
      layout();
    }
  }

  public int getMinimum() {
    return minimum;
  }

  public void setMaximum( int maximum ) {
    if( this.maximum != maximum && maximum >= 0 && maximum > minimum ) {
      this.maximum = maximum;
      adjustThumb();
      adjustSelection();
      layout();
    }
  }

  public int getMaximum() {
    return maximum;
  }

  public void setThumb( int thumb ) {
    if( this.thumb != thumb && thumb >= 1 ) {
      this.thumb = thumb;
      adjustThumb();
      adjustSelection();
      layout();
    }
  }

  public int getThumb() {
    return thumb;
  }

  public void setIncrement( int increment ) {
    if( this.increment != increment ) {
      this.increment = increment;
      layout();
    }
  }

  public int getIncrement() {
    return increment;
  }

  public void setPageIncrement( int pageIncrement ) {
    this.pageIncrement = pageIncrement;
  }

  public int getPageIncrement() {
    return pageIncrement;
  }

  public void setSelection( int selection ) {
    if( !onDrag ) {
      updateSelection( selection );
    }
  }

  public int getSelection() {
    return selection;
  }

  public void addSelectionListener( SelectionListener selectionListener ) {
    listeners.add( selectionListener );
  }

  public void removeSelectionListener( SelectionListener selectionListener ) {
    listeners.remove( selectionListener );
  }

  @Override
  public void addListener( int eventType, final Listener listener ) {
    if( eventType == SWT.Selection ) {
      addSelectionListener( new UntypedSelectionAdapter( listener ) );
    } else {
      super.addListener( eventType, listener );
    }
  }

  @Override
  public void removeListener( int eventType, Listener listener ) {
    if( eventType == SWT.Selection ) {
      removeSelectionListener( lookup( listeners, listener ) );
    } else {
      super.removeListener( eventType, listener );
    }
  }

  @Override
  public void layout() {
    direction.layout( this, buttonLength );
    update();
  }

  public void setIncrementButtonLength( int length ) {
    this.buttonLength = length;
    layout();
  }

  public int getIncrementButtonLength() {
    return buttonLength;
  }

  public void setIncrementColor( Color color ) {
    up.setForeground( color );
    down.setForeground( color );
  }

  public Color getIncrementColor() {
    return up.getForeground();
  }

  public void setPageIncrementColor( Color color ) {
    upFast.setForeground( color );
    downFast.setForeground( color );
  }

  public Color getPageIncrementColor() {
    return upFast.getForeground();
  }

  public void setThumbColor( Color color ) {
    drag.setForeground( color );
  }

  public Color getThumbColor() {
    return drag.getForeground();
  }

  @Override
  public void setBackground( Color color ) {
    up.setBackground( color );
    upFast.setBackground( color );
    drag.setBackground( color );
    downFast.setBackground( color );
    down.setBackground( color );
    super.setBackground( color );
  }

  protected void setSelectionInternal( int selection, int detail ) {
    int oldSelection = this.selection;
    updateSelection( selection );
    if( oldSelection != this.selection ) {
      notifyListeners( detail );
    }
  }

  private void updateSelection( int selection ) {
    if( this.selection != selection ) {
      this.selection = selection;
      adjustSelection();
      layout();
    }
  }

  public void notifyListeners( int detail ) {
    updateOnDrag( detail );
    SelectionEvent selectionEvent = createEvent( detail );
    for( SelectionListener listener : listeners ) {
      listener.widgetSelected( selectionEvent );
    }
  }

  private void updateOnDrag( int detail ) {
    onDrag = ( onDrag || ( SWT.DRAG & detail ) > 0 ) && ( SWT.NONE != detail );
  }

  private SelectionEvent createEvent( int detail ) {
    Event event = new Event();
    event.widget = this;
    event.detail = detail;
    return new SelectionEvent( event );
  }

  private void adjustThumb() {
    if( thumb > maximum - minimum ) {
      thumb = Math.min( maximum - minimum, thumb );
      thumb = Math.max( 1, thumb );
    }
  }

  private void adjustSelection() {
    selection = Math.min( selection, maximum - thumb );
    selection = Math.max( selection, minimum );
  }

  private static Direction getDirection( int style ) {
    return ( style & SWT.HORIZONTAL ) > 0 ? Direction.HORIZONTAL : Direction.VERTICAL;
  }

  private void setDefaultColorScheme() {
    up.setForeground( Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_NORMAL_SHADOW ) );
    upFast.setForeground( Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ) );
    drag.setForeground( Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_FOREGROUND ) );
    downFast.setForeground( Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_BACKGROUND ) );
    down.setForeground( Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_NORMAL_SHADOW ) );
    setBackground( getBackground() );
  }
}