package com.codeaffine.eclipse.swt.util;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

public class ReadAndDispatch {

  public void spinLoop( Widget widget ) {
    while( !widget.isDisposed() ) {
      Display display = widget.getDisplay();
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
  }
}