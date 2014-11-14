package com.codeaffine.eclipse.swt.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.widgets.Shell;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class ReadAndDispatchTest {

  private final class ShellDisposer implements Runnable {

    private final Shell shell;

    private ShellDisposer( Shell shell ) {
      this.shell = shell;
    }

    @Override
    public void run() {
      shell.dispose();
    }
  }

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void spinLoop() {
    Shell shell = openShell();
    scheduleShellDisposer( shell );
    ReadAndDispatch readAndDispatch = new ReadAndDispatch();

    readAndDispatch.spinLoop( shell );

    assertThat( shell.isDisposed() ).isTrue();
  }

  private Shell openShell() {
    Shell result = displayHelper.createShell();
    result.open();
    return result;
  }

  private void scheduleShellDisposer( Shell shell ) {
    displayHelper.getDisplay().timerExec( 10, new ShellDisposer( shell ) );
  }
}