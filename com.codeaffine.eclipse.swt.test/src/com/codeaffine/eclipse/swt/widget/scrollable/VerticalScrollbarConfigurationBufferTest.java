/**
 * Copyright (c) 2014 - 2017 Frank Appel
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.ShellHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

public class VerticalScrollbarConfigurationBufferTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void hasChanged() {
    VerticalScrollbarConfigurationBuffer buffer = createBufferWithScrollable( SWT.V_SCROLL );

    boolean actual = buffer.hasChanged();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChangedOnIncrementChange() {
    Composite scrollable = createScrollable( SWT.V_SCROLL );
    VerticalScrollbarConfigurationBuffer buffer = createBuffer( scrollable );
    scrollable.getVerticalBar().setIncrement( 44 );

    boolean actual = buffer.hasChanged();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChangedOnMaximumChange() {
    Composite scrollable = createScrollable( SWT.V_SCROLL );
    VerticalScrollbarConfigurationBuffer buffer = createBuffer( scrollable );
    scrollable.getVerticalBar().setMaximum( 55 );

    boolean actual = buffer.hasChanged();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChangedOnPageIncrementChange() {
    Composite scrollable = createScrollable( SWT.V_SCROLL );
    VerticalScrollbarConfigurationBuffer buffer = createBuffer( scrollable );
    scrollable.getVerticalBar().setPageIncrement( 11 );

    boolean actual = buffer.hasChanged();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChangedOnThumbChange() {
    Composite scrollable = createScrollable( SWT.V_SCROLL );
    VerticalScrollbarConfigurationBuffer buffer = createBuffer( scrollable );
    scrollable.getVerticalBar().setThumb( 77 );

    boolean actual = buffer.hasChanged();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChangedOnSelectionChange() {
    Composite scrollable = createScrollable( SWT.V_SCROLL );
    VerticalScrollbarConfigurationBuffer buffer = createBuffer( scrollable );
    scrollable.getVerticalBar().setSelection( 10 );

    boolean actual = buffer.hasChanged();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChangedIfNoScrollbarExists() {
    VerticalScrollbarConfigurationBuffer buffer = createBufferWithScrollable( SWT.NONE );

    boolean actual = buffer.hasChanged();

    assertThat( actual ).isTrue();
  }

  private VerticalScrollbarConfigurationBuffer createBufferWithScrollable( int scrollableStyle ) {
    return createBuffer( createScrollable( scrollableStyle ) );
  }

  private Composite createScrollable( int scrollableStyle ) {
    Shell shell = ShellHelper.createShell( displayHelper );
    return new Composite( shell, scrollableStyle );
  }

  private static VerticalScrollbarConfigurationBuffer createBuffer( Composite scrollable ) {
    return new VerticalScrollbarConfigurationBuffer( new ScrollableControl<>( scrollable ) );
  }
}
