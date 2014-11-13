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