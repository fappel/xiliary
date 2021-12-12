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
package com.codeaffine.eclipse.swt.testhelper;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;

import com.codeaffine.eclipse.swt.util.MouseDownActionTimer;

public class MouseDownActionTimerHelper {

  public static void waitTillMouseDownTimerHasBeenTriggered() {
    long start = System.currentTimeMillis();
    long actual = System.currentTimeMillis();
    while( actual - start < MouseDownActionTimer.INITIAL_DELAY + 50 ) {
      actual = System.currentTimeMillis();
      flushPendingEvents();
    }
  }

  private MouseDownActionTimerHelper() {}
}