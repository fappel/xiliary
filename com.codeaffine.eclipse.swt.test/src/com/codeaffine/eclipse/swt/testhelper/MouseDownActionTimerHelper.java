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