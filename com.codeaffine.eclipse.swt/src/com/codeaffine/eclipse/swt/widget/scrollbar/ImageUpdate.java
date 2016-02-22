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

  void setForeground( Color color ) {
    imageDrawer.setForeground( color );
  }

  Color getForeground() {
    return imageDrawer.getForeground();
  }

  void setBackground( Color color ) {
    imageDrawer.setBackground( color );
  }

  Color getBackground() {
    return imageDrawer.getBackground();
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