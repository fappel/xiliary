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

import static java.lang.Math.min;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

class ImageDrawer {

  private final int maxExpansion;

  private Color background;
  private Color foreground;

  ImageDrawer( int expansion  ) {
    this.maxExpansion = expansion;
    this.foreground = Display.getCurrent().getSystemColor( SWT.COLOR_WIDGET_DARK_SHADOW );
    this.background = Display.getCurrent().getSystemColor( SWT.COLOR_LIST_BACKGROUND );
  }

  void setForeground( Color foreground ) {
    if( foreground != null ) {
      this.foreground = foreground;
    }
  }

  Color getForeground() {
    return foreground;
  }

  void setBackground( Color background ) {
    if( background != null ) {
      this.background = background;
    }
  }

  Color getBackground() {
    return background;
  }

  Image draw( int width, int height ) {
    Image result = new Image( getDisplay(), width, height );
    GC gc = new GC( result );
    try {
      draw( gc, width, height );
    } finally {
      gc.dispose();
    }
    return result;
  }

  private void draw( GC gc, int width, int height ) {
    gc.setBackground( background );
    gc.fillRectangle( 0, 0, width, height );
    gc.setBackground( foreground );
    gc.setAdvanced( true );
    gc.setAntialias( SWT.ON );
    int arc = min( width, height ) == 1 ? 1 : maxExpansion + 2;
    gc.fillRoundRectangle( 0, 0, width, height, arc, arc );
  }

  private static Display getDisplay() {
    return Display.getCurrent();
  }
}