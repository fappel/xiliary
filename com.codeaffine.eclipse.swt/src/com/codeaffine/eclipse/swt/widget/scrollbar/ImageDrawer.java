package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

class ImageDrawer {

  private final int maxExpansion;
  private final int colorCode;

  ImageDrawer( int expansion, int colorCode  ) {
    this.maxExpansion = expansion;
    this.colorCode = colorCode;
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
    gc.setBackground( getDragColor() );
    gc.setAdvanced( true );
    gc.setAntialias( SWT.ON );
    gc.setAlpha( 170 );
    gc.fillRoundRectangle( 0, 0, width, height, maxExpansion + 2, maxExpansion + 2 );
  }

  private static Display getDisplay() {
    return Display.getCurrent();
  }

  private Color getDragColor() {
    return getDisplay().getSystemColor( colorCode );
  }
}