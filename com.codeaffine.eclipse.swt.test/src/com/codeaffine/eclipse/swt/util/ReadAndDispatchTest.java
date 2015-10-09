package com.codeaffine.eclipse.swt.util;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch.ProblemHandler;

public class ReadAndDispatchTest {

  private static final int DURATION = 10;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;

  @Before
  public void setUp() {
    shell = openShell();
  }

  @Test
  public void spinLoop() {
    ReadAndDispatch readAndDispatch = new ReadAndDispatch();
    displayHelper.getDisplay().timerExec( 10, () -> shell.dispose() );

    readAndDispatch.spinLoop( shell );

    assertThat( shell.isDisposed() ).isTrue();
  }

  @Test
  public void spinLoopWithDuration() {
    ReadAndDispatch readAndDispatch = new ReadAndDispatch();
    long start = System.currentTimeMillis();

    readAndDispatch.spinLoop( shell, DURATION );
    long actual = System.currentTimeMillis() - start;

    assertThat( actual ).isGreaterThanOrEqualTo( DURATION );
  }

  @Test
  public void spinLoopWithProblem() {
    ReadAndDispatch readAndDispatch = new ReadAndDispatch();
    RuntimeException expected = new RuntimeException();
    displayHelper.getDisplay().timerExec( 10, () -> { throw expected; } );

    Throwable actual = thrownBy( () -> readAndDispatch.spinLoop( shell ) );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void spinLoopWithErrorBoxHandler() {
    boolean[] problemShellOpened = new boolean[ 1 ];
    Shell problemShell = displayHelper.createShell();
    ReadAndDispatch readAndDispatch = new ReadAndDispatch( adaptErrorBoxHandler( problemShell ) );
    setupProblemTriggerAndStateCapturing( problemShell, problemShellOpened );

    readAndDispatch.spinLoop( shell );

    assertThat( problemShellOpened[ 0 ] ).isTrue();
    assertThat( problemShell.isDisposed() ).isTrue();
    assertThat( shell.isDisposed() ).isTrue();
  }

  @Test
  public void openErrorDialog() {
    Shell problemShell = displayHelper.createShell();
    boolean[] problemShellOpened = new boolean[ 1 ];
    displayHelper.getDisplay().timerExec( 10, () -> { captureOpenStateAndClose( problemShell, problemShellOpened ); } );

    ReadAndDispatch.openErrorDialog( problemShell, new RuntimeException() );

    assertThat( problemShellOpened[ 0 ] ).isTrue();
    assertThat( problemShell.isDisposed() ).isTrue();
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsProblemHandler() {
    new ReadAndDispatch( null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void openErrorDialogWithNullAsShell() {
    ReadAndDispatch.openErrorDialog( null, new RuntimeException() );
  }

  @Test( expected = IllegalArgumentException.class )
  public void openErrorDialogWithNullAsProblem() {
    ReadAndDispatch.openErrorDialog( shell, null );
  }

  private Shell openShell() {
    Shell result = displayHelper.createShell();
    result.open();
    return result;
  }

  private static ProblemHandler adaptErrorBoxHandler( Shell problemShell ) {
    return ( shell, problem ) -> ReadAndDispatch.ERROR_BOX_HANDLER.handle( problemShell, problem );
  }

  private void setupProblemTriggerAndStateCapturing( Shell problemShell, boolean[] openedState ) {
    Display display = displayHelper.getDisplay();
    display.timerExec( 10, () -> { throw new RuntimeException(); } );
    display.timerExec( 20, () -> { captureOpenStateAndClose( problemShell, openedState ); } );
    display.timerExec( 30, () -> shell.dispose() );
  }

  private static void captureOpenStateAndClose( Shell problemShell, boolean[] problemShellOpened ) {
    captureDisposeState( problemShell, problemShellOpened ); problemShell.dispose();
  }

  private static boolean captureDisposeState( Shell shell, boolean[] isOpen ) {
    return isOpen[ 0 ] = !shell.isDisposed();
  }
}