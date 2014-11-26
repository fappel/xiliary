package com.codeaffine.eclipse.swt.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.swt.util.MouseDownActionTimer.TimerAction;

public class MouseDownActionTimerTest {

  private MouseDownActionTimer timer;
  private TimerAction timerAction;
  private ButtonClick mouseClick;
  private Display display;

  @Before
  public void setUp() {
    timerAction = mock( TimerAction.class );
    mouseClick = mock( ButtonClick.class );
    display = mock( Display.class );
    timer = new MouseDownActionTimer( timerAction, mouseClick, display );
  }

  @Test
  public void activate() {
    enableTimerAction();

    timer.activate();

    verify( display ).timerExec( MouseDownActionTimer.INITIAL_DELAY, timer );
  }

  @Test
  public void activateIfTimerActionIsDisabled() {
    timer.activate();

    verify( display, never() ).timerExec( MouseDownActionTimer.INITIAL_DELAY, timer );
  }

  @Test
  public void run() {
    enableTimerAction();
    simulateLeftButtonMouseDown();

    timer.run();

    verify( timerAction ).run();
    verify( display ).timerExec( MouseDownActionTimer.FAST_DELAY, timer );
  }

  @Test
  public void runIfTimerActionIsDisabled() {
    simulateLeftButtonMouseDown();

    timer.run();

    verify( timerAction, never() ).run();
    verify( display, never() ).timerExec( MouseDownActionTimer.FAST_DELAY, timer );
  }

  @Test
  public void runIfLeftMouseButttonIsNotPressed() {
    enableTimerAction();

    timer.run();

    verify( timerAction, never() ).run();
    verify( display, never() ).timerExec( MouseDownActionTimer.FAST_DELAY, timer );
  }

  private void enableTimerAction() {
    when( timerAction.isEnabled() ).thenReturn( true );
  }

  private void simulateLeftButtonMouseDown() {
    when( mouseClick.isArmed() ).thenReturn( true );
  }
}