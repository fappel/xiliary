/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.test.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.junit.Rule;
import org.junit.Test;

public class ShellHelperTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void createShell() {
    Shell actual = ShellHelper.createShell( displayHelper );

    assertThat( actual.getBounds() ).isEqualTo( ShellHelper.DEFAULT_BOUNDS );
    assertThat( actual.getLayout() ).isInstanceOf( FillLayout.class );
  }

  @Test
  public void createShellWithStyle() {
    Shell actual = ShellHelper.createShell( displayHelper, SWT.SHELL_TRIM );

    assertThat( actual.getStyle() & SWT.SHELL_TRIM ).isEqualTo( SWT.SHELL_TRIM );
    assertThat( actual.getBounds() ).isEqualTo( ShellHelper.DEFAULT_BOUNDS );
    assertThat( actual.getLayout() ).isInstanceOf( FillLayout.class );
  }

  @Test
  public void createShellWithoutLayout() {
    Shell actual = ShellHelper.createShellWithoutLayout( displayHelper, SWT.SHELL_TRIM );

    assertThat( actual.getLayout() ).isNull();
    assertThat( actual.getBounds() ).isEqualTo( ShellHelper.DEFAULT_BOUNDS );
    assertThat( actual.getStyle() & SWT.SHELL_TRIM ).isEqualTo( SWT.SHELL_TRIM );
  }
}