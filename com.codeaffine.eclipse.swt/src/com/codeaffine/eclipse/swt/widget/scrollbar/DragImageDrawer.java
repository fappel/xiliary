package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

class DragImageDrawer {

  private final int maxExpansion;

  DragImageDrawer( int expansion ) {
    this.maxExpansion = expansion;
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
    gc.fillRoundRectangle( 0, 0, width, height, maxExpansion, maxExpansion );
  }

  private static Display getDisplay() {
    return Display.getCurrent();
  }

  private static Color getDragColor() {
    return getDisplay().getSystemColor( SWT.COLOR_WIDGET_FOREGROUND );
  }
}