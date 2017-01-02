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

import static java.lang.Math.abs;

public class SelectionRaster {

  private final FlatScrollBar scrollBar;
  private final int rasterSize;

  public SelectionRaster( FlatScrollBar scrollBar, int rasterSize ) {
    this.scrollBar = scrollBar;
    this.rasterSize = rasterSize;
  }

  public void updateSelection( int selection ) {
    int rasterValue = calculateRasterValue( selection );
    int scrollBarSelection = scrollBar.getSelection();
    if( isDifferentRasterSection( rasterValue, scrollBarSelection ) ) {
      scrollBar.setSelection( rasterValue );
    }
  }

  public int calculateRasterValue( int selection ) {
    return ( selection / rasterSize ) * rasterSize;
  }

  private boolean isDifferentRasterSection( int rasterValue, int scrollBarSelection ) {
    return abs( rasterValue - scrollBarSelection ) >= rasterSize;
  }
}