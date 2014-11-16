package com.codeaffine.eclipse.swt.util;

import org.eclipse.swt.widgets.Display;

public class ActionScheduler {

  private final Display display;
  private final Runnable action;

  public ActionScheduler( Display display, Runnable action ) {
    this.display = display;
    this.action = action;
  }

  public void schedule( int delay ) {
    display.timerExec( delay, action );
  }
}
