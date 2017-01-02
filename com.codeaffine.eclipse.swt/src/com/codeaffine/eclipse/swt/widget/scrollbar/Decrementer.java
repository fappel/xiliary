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

import org.eclipse.swt.SWT;

import com.codeaffine.eclipse.swt.widget.scrollbar.ClickControl.ClickAction;

class Decrementer implements ClickAction {

  private final FlatScrollBar scrollBar;

  Decrementer( FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
  }

  @Override
  public void run() {
    int selection = scrollBar.getSelection() - scrollBar.getIncrement();
    scrollBar.setSelectionInternal( selection, SWT.ARROW_UP );
  }

  @Override
  public void setCoordinates( int x, int y ) {
  }
}