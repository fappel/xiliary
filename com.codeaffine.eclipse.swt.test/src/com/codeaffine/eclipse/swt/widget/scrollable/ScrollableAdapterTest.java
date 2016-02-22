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
package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.util.PlatformTypeHelper.getUnusedTypes;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.IllegalComponentStateException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.Platform;
import com.codeaffine.eclipse.swt.util.Platform.PlatformType;

@SuppressWarnings("unchecked")
public class ScrollableAdapterTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private TestScrollableFactory testScrollableFactory;
  private Platform platform;
  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    platform = new Platform();
    testScrollableFactory = new TestScrollableFactory();
  }

  @Test
  public void getScrollable() {
    ScrollableAdapter<Scrollable >adapter = createAdapter();

    Scrollable actual = adapter.getScrollable();

    assertThat( actual ).isSameAs( testScrollableFactory.getScrollable() );
    assertThat( new ScrollableAdapterFactory().isAdapted( actual ) ).isTrue();
  }

  @Test
  public void setLayout() {
    ScrollableAdapter<Scrollable >adapter = createAdapter();

    Throwable actual = thrownBy( () -> adapter.setLayout( null ) );

    assertThat( actual )
      .isExactlyInstanceOf( UnsupportedOperationException.class )
      .hasMessageContaining( ScrollableAdapter.class.getName() );
  }

  @Test
  public void scrollableParentRelationDoesNotMatch() {
    ScrollableFactory<Scrollable> factory = composite -> new Text( shell, SWT.NONE );

    Throwable actual = thrownBy( () ->  new ScrollableAdapter<Scrollable>( shell, platform, factory ) );

    assertThat( actual ).isInstanceOf( IllegalComponentStateException.class );
  }

  @Test
  public void getLayoutWithoutLayoutMapping() {
    ScrollableAdapter<Scrollable >adapter = createAdapter();

    Layout layout = adapter.getLayout();

    assertThat( layout ).isExactlyInstanceOf( FillLayout.class );
  }

  @Test
  public void getLayoutWithMatchingLayoutMapping() {
    RowLayout expected = new RowLayout();
    LayoutMapping<Scrollable> mapping = createLayoutMapping( expected, PlatformType.values() );
    ScrollableAdapter<Scrollable >adapter = createAdapter( mapping );

    Layout layout = adapter.getLayout();

    assertThat( layout ).isSameAs( expected );
  }

  @Test
  public void getLayoutWithNonMatchingLayoutMapping() {
    LayoutMapping<Scrollable> mapping = createLayoutMapping( new RowLayout(), getUnusedTypes() );
    ScrollableAdapter<Scrollable >adapter = createAdapter( mapping );

    Layout layout = adapter.getLayout();

    assertThat( layout ).isExactlyInstanceOf( FillLayout.class );
  }

  private static LayoutMapping<Scrollable> createLayoutMapping( Layout expected, PlatformType ... types ) {
    return new LayoutMapping<Scrollable>( new TestLayoutFactory( expected ), types );
  }

  private ScrollableAdapter<Scrollable> createAdapter( LayoutMapping<Scrollable> ... layoutMappings ) {
    return new ScrollableAdapter<Scrollable>( shell, platform, testScrollableFactory, layoutMappings );
  }
}