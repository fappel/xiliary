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
package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.codeaffine.eclipse.swt.widget.scrollbar.ClickControl.ClickAction;

class FastDecrementer implements ClickAction {

  private final FlatScrollBar scrollBar;

  private int x;
  private int y;

  FastDecrementer( FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
  }

  @Override
  public void run() {
    Rectangle drag = getDragBounds();
    Point mouse = getMouseLocation();
    if( mouse.x <= drag.x || mouse.y <= drag.y ) {
      int selection = scrollBar.getSelection() - scrollBar.getPageIncrement();
      scrollBar.setSelectionInternal( selection, SWT.PAGE_UP );
    }
  }

  @Override
  public void setCoordinates( int x, int y ) {
    this.x = x;
    this.y = y;
  }

  private Point getMouseLocation() {
    return getDisplay().map( scrollBar.upFast.getControl(), null, x, y );
  }

  private Rectangle getDragBounds() {
    Rectangle dragBounds = scrollBar.drag.getControl().getBounds();
    return getDisplay().map( scrollBar, null, dragBounds );
  }

  private Display getDisplay() {
    return scrollBar.getDisplay();
  }
}