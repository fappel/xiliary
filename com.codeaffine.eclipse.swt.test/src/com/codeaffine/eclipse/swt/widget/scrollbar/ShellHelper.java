package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

class ShellHelper {

  static Shell createShell( DisplayHelper displayHelper ) {
    return createShell( displayHelper, SWT.NONE );
  }

  static Shell createShell( DisplayHelper displayHelper, int style ) {
    Shell result = displayHelper.createShell( style );
    result.setSize( 200, 200 );
    result.setLayout( new FillLayout() );
    return result;
  }
}