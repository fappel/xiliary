/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.util;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;

public class ButtonClick {

  public static final int LEFT_BUTTON = 1;

  private boolean armed;

  public boolean isArmed() {
    return armed;
  }

  public void arm( MouseEvent event ) {
    if( event.button == LEFT_BUTTON ) {
      armed = true;
    }
  }

  public void disarm() {
    armed = false;
  }

  public void trigger( MouseEvent event, Runnable action ) {
    try {
      doTrigger( event, action );
    } finally {
      disarm();
    }
  }

  private void doTrigger( MouseEvent event, Runnable action ) {
    if( armed && inRange( event ) ) {
      action.run();
    }
  }

  private static boolean inRange( MouseEvent event ) {
    Point size = ( ( Control )event.widget ).getSize();
    return event.x >= 0 && event.x <= size.x && event.y >= 0 && event.y <= size.y;
  }
}