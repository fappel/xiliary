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
package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class LayoutTriggerTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void pull() {
    Shell shell = createShellWithoutLayoutManager();
    Control control = addChild( shell );
    addLayoutManager( shell );
    LayoutTrigger trigger = new LayoutTrigger( shell );

    trigger.pull();

    assertThat( control.getBounds() ).isEqualTo( shell.getClientArea() );
  }

  private Shell createShellWithoutLayoutManager() {
    Shell result = displayHelper.createShell();
    result.setSize( 100, 100 );
    result.open();
    return result;
  }

  private static Control addChild( Shell shell ) {
    Control control = new Label( shell, SWT.NONE );
    return control;
  }

  private static void addLayoutManager( Shell shell ) {
    shell.setLayout( new FillLayout() );
  }
}