/**
 * Copyright (c) 2014 - 2017 Frank Appel
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
import org.eclipse.swt.graphics.Point;

import com.codeaffine.eclipse.swt.widget.scrollbar.DragControl.DragAction;

final class DragShifter implements DragAction {

  private final FlatScrollBar scrollBar;
  private final int buttonLength;

  public DragShifter( FlatScrollBar scrollBar, int buttonLength ) {
    this.scrollBar = scrollBar;
    this.buttonLength = buttonLength;
  }

  @Override
  public void start() {
    scrollBar.notifyListeners( SWT.DRAG );
  }

  @Override
  public void run( int startX, int startY, int currentX, int currentY ) {
    ShiftData shiftData = newShiftData( startX, startY, currentX, currentY );
    if( shiftData.canShift() ) {
      int selectionRange = calculateSelectionRange( scrollBar );
      int selectionDelta = shiftData.calculateSelectionDelta( selectionRange );
      int selection = scrollBar.getSelection() + selectionDelta;
      scrollBar.setSelectionInternal( selection, SWT.DRAG );
    }
  }

  @Override
  public void end() {
    scrollBar.notifyListeners( SWT.NONE );
  }

  private ShiftData newShiftData( int startX, int startY, int currentX, int currentY ) {
    ShiftData result;
    if( scrollBar.direction == HORIZONTAL ) {
      result = new ShiftData( buttonLength, getScrollBarSize().x, getDragSize().x, currentX - startX );
    } else {
      result = new ShiftData( buttonLength, getScrollBarSize().y, getDragSize().y, currentY - startY );
    }
    return result;
  }

  private Point getScrollBarSize() {
    return scrollBar.getSize();
  }

  private Point getDragSize() {
    return scrollBar.drag.getControl().getSize();
  }
}