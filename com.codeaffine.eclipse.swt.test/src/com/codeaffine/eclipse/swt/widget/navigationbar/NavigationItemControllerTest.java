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
package com.codeaffine.eclipse.swt.widget.navigationbar;

import static com.codeaffine.eclipse.swt.test.util.graphics.PointAssert.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class NavigationItemControllerTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private NavigationItemController controller;

  @Before
  public void setUp() {
    controller = new NavigationItemControllerStub();
  }

  @Test
  public void getAddControlBuilderDefaults() {
    Control actual = controller.getAddControlBuilder().build( displayHelper.createShell() );
    Point preferredSize = actual.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( preferredSize ).isEqualToPointOf( 0, 0 );
  }

  @Test
  public void getRemoveControlBuilderDefaults() {
    Control actual = controller.getRemoveControlBuilder().build( displayHelper.createShell() );
    Point preferredSize = actual.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( preferredSize ).isEqualToPointOf( 0, 0 );
  }
}