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
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class ScrollBarAdapter extends ScrollBar {

  private FlatScrollBar scrollBar;

  private ScrollBarAdapter() {
    super( null, -1 );
  }

  public void setScrollbar( FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
  }

  @Override
  public Point getSize() {
    Point result = new Point( 0, 0 );
    if( scrollBar != null ) {
    	result = scrollBar.getSize();
    }
	return result;
  }

  @Override
  public void setSelection( int selection ) {
    scrollBar.setSelection( selection );
    scrollBar.notifyListeners( SWT.None );
  }

  @Override
  public int getSelection() {
    return scrollBar.getSelection();
  }

  @Override
  public void addListener( int eventType, Listener listener ) {
    if( eventType == SWT.Selection ) {
      scrollBar.addListener( eventType, listener );
    } else {
      super.addListener( eventType, listener );
    }
  }

  @Override
  public boolean isVisible() {
    return scrollBar.isVisible();
  }
}