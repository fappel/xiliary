package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static com.codeaffine.eclipse.swt.testhelper.MouseDownActionTimerHelper.waitTillMouseDownTimerHasBeenTriggered;
import static com.codeaffine.eclipse.swt.widget.scrollbar.ShellHelper.createShell;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.ClickControl.ClickAction;

public class ClickControlTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ClickControl clickControl;
  private ClickAction action;

  @Before
  public void setUp() {
    Shell shell = createShell( displayHelper, SWT.SHELL_TRIM );
    action = mock( ClickAction.class );
    clickControl = new ClickControl( shell, getSystemColorBlue(), action );
    shell.open();
  }

  @Test
  public void getControl() {
    Control control = clickControl.getControl();

    assertThat( control.getBackground() ).isEqualTo( getSystemColorBlue() );
    assertThat( control.getLayoutData() ).isNull();
  }

  @Test
  public void clickOnControl() {
    triggerLeftButtonMouseEvent( SWT.MouseDown );
    triggerLeftButtonMouseEvent( SWT.MouseUp );

    verify( action ).run();
  }

  @Test
  public void mouseUpOnly() {
    triggerLeftButtonMouseEvent( SWT.MouseUp );

    verify( action, never() ).run();
  }

  @Test
  public void mouseDownTimerActivation() {
    triggerLeftButtonMouseEvent( SWT.MouseDown );
    waitTillMouseDownTimerHasBeenTriggered();

    InOrder order = inOrder( action );
    order.verify( action, atLeastOnce() ).setCoordinates( 1, 1 );
    order.verify( action, atLeastOnce() ).run();
  }

  private void triggerLeftButtonMouseEvent( int event ) {
    trigger( event ).at( 1, 1 ).withButton( SWT.BUTTON1 ).on( clickControl.getControl() );
  }

  private static Color getSystemColorBlue() {
    return Display.getCurrent().getSystemColor( SWT.COLOR_BLUE );
  }
}