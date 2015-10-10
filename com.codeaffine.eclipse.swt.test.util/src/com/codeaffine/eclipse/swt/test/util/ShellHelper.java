package com.codeaffine.eclipse.swt.test.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Shell;

public class ShellHelper {

  public static final Rectangle DEFAULT_BOUNDS = new Rectangle( 400, 300, 200, 200 );

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
    result.setBounds( DEFAULT_BOUNDS );
    return result;
  }

  public static Rectangle computeTrim( Decorations decorations, Rectangle bounds ) {
    return decorations.computeTrim( bounds.x, bounds.y, bounds.width, bounds.height );
  }
}