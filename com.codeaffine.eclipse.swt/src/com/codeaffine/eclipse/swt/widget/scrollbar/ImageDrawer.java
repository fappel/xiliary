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

import static java.lang.Math.min;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

class ImageDrawer {

  static final String IMAGE_DRAWER_IS_DISPOSED = "ImageDrawer is disposed.";

  private final int maxExpansion;

  private Color background;
  private Color foreground;

  ImageDrawer( int expansion  ) {
    this( expansion, getSystemColor( SWT.COLOR_WIDGET_DARK_SHADOW ), getSystemColor( SWT.COLOR_LIST_BACKGROUND ) );
  }

  ImageDrawer( int expansion, Color background, Color foreground ) {
    this.maxExpansion = expansion;
    this.foreground = defensiveCopy( background );
    this.background = defensiveCopy( foreground );
  }

  void setForeground( Color foreground ) {
    checkDisposed();
    if( foreground != null ) {
      this.foreground = prepareColorAttribute( this.foreground, foreground );
    }
  }

  Color getForeground() {
    checkDisposed();
    return foreground;
  }

  void setBackground( Color background ) {
    checkDisposed();
    if( background != null ) {
      this.background = prepareColorAttribute( this.background, background );
    }
  }

  Color getBackground() {
    checkDisposed();
    return background;
  }

  Image draw( int width, int height ) {
    checkDisposed();
    Image result = new Image( getDisplay(), width, height );
    GC gc = new GC( result );
    try {
      draw( gc, width, height );
    } finally {
      gc.dispose();
    }
    return result;
  }

  boolean isDisposed() {
    return background.isDisposed();
  }

  void dispose() {
    if( !isDisposed() ) {
      this.background.dispose();
      this.foreground.dispose();
    }
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

  private void checkDisposed() {
    if( isDisposed() ) {
      throw new IllegalStateException( IMAGE_DRAWER_IS_DISPOSED );
    }
  }

  private static Color getSystemColor( int colorCode ) {
    return getDisplay().getSystemColor( colorCode );
  }

  private static Color prepareColorAttribute( Color oldColor, Color newColor ) {
    oldColor.dispose();
    return defensiveCopy( newColor );
  }

  private static Color defensiveCopy( Color background ) {
    return new Color( getDisplay(), background.getRGB() );
  }

  private static Display getDisplay() {
    return Display.getCurrent();
  }
}