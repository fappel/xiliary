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

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.Platform;
import com.codeaffine.eclipse.swt.util.Platform.PlatformType;

public class FlatScrollBarTableTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private TestTableFactory testTableFactory;
  private Platform platform;
  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    platform = mock( Platform.class );
    testTableFactory = new TestTableFactory();
    shell.open();
  }

  @Test
  public void getTree() {
    FlatScrollBarTable adapter = createTableAdapter();

    Table actual = adapter.getTable();

    assertThat( actual ).isSameAs( testTableFactory.getTable() );
  }

  @Test
  public void getLayoutIfPlatformMatches() {
    stubPlatformToMatchAnyType( platform );
    FlatScrollBarTable adapter = createTableAdapter();

    Layout actual = adapter.getLayout();

    assertThat( actual ).isExactlyInstanceOf( ScrollableLayout.class );
  }

  @Test
  public void getLayoutIfPlatformDoesNotMatch() {
    FlatScrollBarTable adapter = createTableAdapter();

    Layout actual = adapter.getLayout();

    assertThat( actual ).isExactlyInstanceOf( FillLayout.class );
  }

  private FlatScrollBarTable createTableAdapter() {
    return new FlatScrollBarTable( shell, platform, testTableFactory, FlatScrollBarTable.createLayoutMapping() );
  }

  private static void stubPlatformToMatchAnyType( Platform platform ) {
    when( platform.matchesOneOf( ( PlatformType[] )anyVararg() ) ).thenReturn( true );
  }
}