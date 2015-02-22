package com.codeaffine.eclipse.swt.util;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

public class ReadAndDispatch {

  public void spinLoop( Widget widget ) {
    spinLoop( widget, Long.MAX_VALUE - System.currentTimeMillis() );
  }

  public void spinLoop( Widget widget, long duration ) {
    long end = System.currentTimeMillis() + duration;
    while( !widget.isDisposed() && ( end - System.currentTimeMillis() ) > 0 ) {
      Display display = widget.getDisplay();
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
  }
}