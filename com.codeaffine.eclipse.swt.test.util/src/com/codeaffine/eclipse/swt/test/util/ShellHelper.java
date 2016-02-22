/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
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

  public static Shell createDemoShell( DisplayHelper displayHelper ) {
    Shell result = createShell( displayHelper, SWT.SHELL_TRIM );
    result.setBackground( displayHelper.getDisplay().getSystemColor( SWT.COLOR_WHITE ) );
    result.setLayout( createDemoShellLayout() );
    return result;
  }

  public static Rectangle computeTrim( Decorations decorations, Rectangle bounds ) {
    return decorations.computeTrim( bounds.x, bounds.y, bounds.width, bounds.height );
  }

  private static FillLayout createDemoShellLayout() {
    FillLayout result = new FillLayout();
    result.marginHeight = 10;
    result.marginWidth = 10;
    return result;
  }
}