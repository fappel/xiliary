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

import static com.codeaffine.eclipse.swt.widget.scrollbar.Direction.HORIZONTAL;
import static com.codeaffine.eclipse.swt.widget.scrollbar.ShiftData.calculateSelectionRange;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class MouseWheelShifter implements Listener, DisposeListener {

  private final FlatScrollBar scrollBar;
  private final Composite parent;
  private final int buttonLength;

  MouseWheelShifter( FlatScrollBar scrollBar, Composite parent, int buttonLength ) {
    this.scrollBar = scrollBar;
    this.parent = parent;
    this.buttonLength = buttonLength;
    parent.addListener( getListenerType(), this );
    scrollBar.addDisposeListener( this );
  }

  @Override
  public void handleEvent( Event event ) {
    handleMouseWheelScroll( event );
  }

  @Override
  public void widgetDisposed( DisposeEvent e ) {
    parent.removeListener( getListenerType(), this );
  }

  private void handleMouseWheelScroll( Event event ) {
    ShiftData shiftData = newShiftData( event.count );
    if( shiftData.canShift() ) {
      int selectionRange = calculateSelectionRange( scrollBar );
      int selectionDelta = shiftData.calculateSelectionDelta( selectionRange );
      int selection = scrollBar.getSelection() - selectionDelta;
      scrollBar.setSelectionInternal( selection, scrollBar.direction.value() );
    }
  }

  private ShiftData newShiftData( int delta ) {
    ShiftData result;
    if( scrollBar.direction == Direction.HORIZONTAL ) {
      result = new ShiftData( buttonLength, getScrollBarSize().x, getDragSize().x, delta );
    } else {
      result = new ShiftData( buttonLength, getScrollBarSize().y, getDragSize().y, delta );
    }
    return result;
  }

  private Point getScrollBarSize() {
    return scrollBar.getSize();
  }

  private Point getDragSize() {
    return scrollBar.drag.getControl().getSize();
  }

  private int getListenerType() {
    return scrollBar.direction == HORIZONTAL ? SWT.MouseHorizontalWheel: SWT.MouseVerticalWheel;
  }
}