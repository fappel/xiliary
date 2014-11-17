package com.codeaffine.eclipse.swt.testhelper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class ShellHelper {

  public static Shell createShell( DisplayHelper displayHelper ) {
    return createShell( displayHelper, SWT.NONE );
  }

  public static Shell createShell( DisplayHelper displayHelper, int style ) {
    Shell result = createShellWithoutLayout( displayHelper, style );
    result.setLayout( new FillLayout() );
    return result;
  }

  public static Shell createShellWithoutLayout( DisplayHelper displayHelper, int style ) {
    Shell result = displayHelper.createShell( style );
    result.setBounds( 400, 300, 200, 200 );
    return result;
  }

  public static void openShell( Shell shell ) {
    shell.open();
    waitForGtkRendering();
  }

  public static void waitForGtkRendering() {
    if( "gtk".equals( SWT.getPlatform() ) ) {
      long start = System.currentTimeMillis();
      while( ( System.currentTimeMillis() - start ) < 500 ) {
        if( !Display.getCurrent().readAndDispatch() ) {}
      }
    }
  }
}