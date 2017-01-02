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
package com.codeaffine.eclipse.swt.util;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

class MouseEventHelper {

  static MouseEvent createMouseEvent( Control control, int x, int y, int stateMask, int button  ) {
    MouseEvent result = new MouseEvent( newEvent( control ) );
    result.x = x;
    result.y = y;
    result.stateMask = stateMask;
    result.button = button;
    return result;
  }

  private static Event newEvent( Control control ) {
    Event result = new Event();
    result.widget = control;
    return result;
  }
}