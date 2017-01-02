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