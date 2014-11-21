package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static com.codeaffine.eclipse.swt.testhelper.MouseDownActionTimerHelper.waitTillMouseDownTimerHasBeenTriggered;
import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.util.MouseClick.LEFT_BUTTON;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.equipScrollBarWithListener;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.verifyNotification;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

@RunWith( value = Parameterized.class )
public class FastIncrementerTest {

  @Parameters
  public static Collection<Object[]> data() {
    return DirectionHelper.valuesForParameterizedTests();
  }

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private final int direction;
  private FlatScrollBar scrollBar;

  public FastIncrementerTest( int direction ) {
    this.direction = direction;
  }

  @Before
  public void setUp() {
    Shell shell = createShell( displayHelper, SWT.SHELL_TRIM );
    scrollBar = new FlatScrollBar( shell, direction );
    shell.open();
  }

  @Test
  public void run() {
    SelectionListener listener = equipScrollBarWithListener( scrollBar );

    triggerLeftButtonMouseDown();
    waitTillTimerHasFiredAtLeastTwice();
    triggerMouseUp();

    SelectionEvent event = verifyNotification( listener );
    assertThat( event.detail ).isEqualTo( SWT.PAGE_DOWN );
    assertThat( scrollBar.getSelection() ).isEqualTo( scrollBar.getPageIncrement() );
  }

  private void triggerLeftButtonMouseDown() {
    trigger( SWT.MouseDown ).at( 1, 1 ).withButton( LEFT_BUTTON ).on( getDownFastControl() );
  }

  private void triggerMouseUp() {
    trigger( SWT.MouseUp ).at( 1, 1 ).on( getDownFastControl() );
  }

  private Control getDownFastControl() {
    return scrollBar.downFast.getControl();
  }

  private static void waitTillTimerHasFiredAtLeastTwice() {
    waitTillMouseDownTimerHasBeenTriggered();
    waitTillMouseDownTimerHasBeenTriggered();
  }
}