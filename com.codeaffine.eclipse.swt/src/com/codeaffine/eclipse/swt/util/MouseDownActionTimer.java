package com.codeaffine.eclipse.swt.util;

import org.eclipse.swt.widgets.Display;

public class MouseDownActionTimer implements Runnable {

  public static final int INITIAL_DELAY = 300;
  public static final int FAST_DELAY = 50;

  private final TimerAction timerAction;
  private final MouseClick mouseClick;
  private final Display display;

  public interface TimerAction extends Runnable {
    boolean isEnabled();
  }

  public MouseDownActionTimer( TimerAction timerAction, MouseClick mouseClick, Display display ) {
    this.timerAction = timerAction;
    this.mouseClick = mouseClick;
    this.display = display;
  }

  public void activate() {
    if( timerAction.isEnabled() ) {
      display.timerExec( INITIAL_DELAY, this );
    }
  }

  @Override
  public void run() {
    if( mouseClick.isArmed() && timerAction.isEnabled() ) {
      timerAction.run();
      display.timerExec( FAST_DELAY, this );
    }
  }
}