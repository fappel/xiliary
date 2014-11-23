package com.codeaffine.eclipse.swt.test.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

class EventQueueHelper {

  static void flushPendingEvents() {
    int repeat = getRepeatCount();
    while( repeat > 0 ) {
      doFlushPendingEvents();
      repeat--;
    }
  }

  // TODO [fappel]: Strange behaviour on cocoa. Single call to Display#readAndDispatch seems
  //                not to be sufficient. This solution was accomplished by try and error.
  private static int getRepeatCount() {
    return "cocoa".equals( SWT.getPlatform() ) ? 3 : 1;
  }

  private static void doFlushPendingEvents() {
    while(    Display.getCurrent() != null
           && !Display.getCurrent().isDisposed()
           && Display.getCurrent().readAndDispatch() ) {}
  }
}