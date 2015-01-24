package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;

class ImageUpdate {

  private final ImageDrawer imageDrawer;
  private final Label control;

  ImageUpdate( Label control, int maxExpansion ) {
    this.imageDrawer = new ImageDrawer( maxExpansion );
    this.control = control;
  }

  void setColor( Color color ) {
    imageDrawer.setColor( color );
  }

  Color getColor() {
    return imageDrawer.getColor();
  }

  void update() {
    if( control.getImage() != null ) {
      control.getImage().dispose();
    }
    Point size = control.getSize();
    if( size.x > 0 && size.y > 0 ) {
      control.setImage( imageDrawer.draw( size.x, size.y ) );
    }
  }
}