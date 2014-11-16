package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static com.codeaffine.eclipse.swt.testhelper.MouseDownActionTimerHelper.waitTillMouseDownTimerHasBeenTriggered;
import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.util.MouseClick.LEFT_BUTTON;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
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
public class FastDecrementerTest {

  @Parameters
  public static Collection<Object[]> data() {
    return OrientationHelper.valuesForParameterizedTests();
  }

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private final Orientation orientation;

  private FlatScrollBar scrollBar;

  public FastDecrementerTest( Orientation orientation ) {
    this.orientation = orientation;
  }

  @Before
  public void setUp() {
    Shell shell = createShell( displayHelper, SWT.SHELL_TRIM );
    scrollBar = new FlatScrollBar( shell, orientation );
    shell.open();
  }

  @Test
  public void run() {
    scrollBar.setSelectionInternal( scrollBar.getPageIncrement() * 2 );
    Point size = getUpFastControl().getSize();

    triggerLeftButtonMouseDown( size );
    waitTillTimerHasFiredAtLeastTwice();
    triggerMouseUp();

    assertThat( scrollBar.getSelection() ).isEqualTo( scrollBar.getPageIncrement() );
  }

  private void triggerLeftButtonMouseDown( Point size ) {
    trigger( SWT.MouseDown ).at( size.x, size.y ).withButton( LEFT_BUTTON ).on( getUpFastControl() );
  }

  private void triggerMouseUp() {
    trigger( SWT.MouseUp ).at( 1, 1 ).on( getUpFastControl() );
  }

  private Control getUpFastControl() {
    return scrollBar.upFast.getControl();
  }

  private static void waitTillTimerHasFiredAtLeastTwice() {
    waitTillMouseDownTimerHasBeenTriggered();
    waitTillMouseDownTimerHasBeenTriggered();
  }
}