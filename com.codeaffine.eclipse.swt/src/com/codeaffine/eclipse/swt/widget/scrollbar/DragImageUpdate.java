package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;

class DragImageUpdate {

  private final Label control;
  private final DragImageDrawer dragImageDrawer;

  DragImageUpdate( Label control, int maxExpansion ) {
    this.dragImageDrawer = new DragImageDrawer( maxExpansion );
    this.control = control;
  }

  void update() {
    if( control.getImage() != null ) {
      control.getImage().dispose();
    }
    Point size = control.getSize();
    if( size.x > 0 && size.y > 0 ) {
      control.setImage( dragImageDrawer.draw( size.x, size.y ) );
    }
  }
}