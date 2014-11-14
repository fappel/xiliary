package com.codeaffine.eclipse.swt.util;

import org.eclipse.swt.widgets.Widget;

public class ReadAndDispatch {

  public void spinLoop( Widget widget ) {
    while( !widget.isDisposed() ) {
      if( !widget.getDisplay().readAndDispatch() ) {
        widget.getDisplay().sleep();
      }
    }
  }
}