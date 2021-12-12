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
import static com.codeaffine.eclipse.swt.widget.scrollable.SelectionEventHelper.createEvent;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ControlReflectionUtil;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

public class StructuredScrollableHorizontalSelectionListenerTest {

  private static final int SELECTION = 5;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;
  private Tree tree;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    tree = createTree( shell, 6, 4, SWT.BORDER );
    shell.open();
  }

  @Test
  public void selectionChanged() {
    AdaptionContext<Tree> context = new AdaptionContext<>( shell, new ScrollableControl<>( tree ) );
    StructuredScrollableHorizontalSelectionListener listener = new StructuredScrollableHorizontalSelectionListener( context );
    Point adapterLocation = shell.getLocation();
    Point location = tree.getLocation();

    listener.widgetSelected( createEvent( shell, SELECTION ) );

    assertThat( tree.getLocation().x ).isEqualTo( -SELECTION - tree.getBorderWidth() );
    assertThat( tree.getLocation().y ).isEqualTo( location.y - tree.getBorderWidth() );
    assertThat( shell.getLocation() ).isEqualTo( adapterLocation );
  }

  @Test
  public void selectionChangedIfHeaderVisible() {
    tree.setHeaderVisible( true );
    AdaptionContext<Tree> context = new AdaptionContext<>( shell, new ScrollableControl<>( tree ) );
    StructuredScrollableHorizontalSelectionListener listener = new StructuredScrollableHorizontalSelectionListener( context );
    Point adapterLocation = shell.getLocation();
    Point location = tree.getLocation();

    listener.widgetSelected( createEvent( shell, SELECTION ) );

    assertThat( tree.getLocation().x ).isEqualTo( -SELECTION - tree.getBorderWidth() );
    assertThat( tree.getLocation().y ).isEqualTo( location.y - tree.getBorderWidth() );
    assertThat( shell.getLocation() ).isEqualTo( adapterLocation );
  }

  @Test
  public void selectionChangedWithReparentedAdapter() {
    Composite adapter = reparentScrollable( new Composite( shell, SWT.NONE ), tree );
    equipShellWithLayoutMargin();
    AdaptionContext<Tree> context = new AdaptionContext<>( adapter, new ScrollableControl<>( tree ) );
    StructuredScrollableHorizontalSelectionListener listener = new StructuredScrollableHorizontalSelectionListener( context );
    Point adapterLocation = shell.getLocation();
    Point location = tree.getLocation();

    listener.widgetSelected( createEvent( shell, SELECTION ) );

    assertThat( tree.getLocation().x ).isEqualTo( location.x - SELECTION - tree.getBorderWidth() );
    assertThat( tree.getLocation().y ).isEqualTo( location.y - tree.getBorderWidth() );
    assertThat( shell.getLocation() ).isEqualTo( adapterLocation );
  }

  @Test
  public void selectionChangedWithHeaderVisibleWithReparentedAdapter() {
    tree.setHeaderVisible( true );
    Composite adapter = reparentScrollable( new Composite( shell, SWT.NONE ), tree );
    equipShellWithLayoutMargin();
    AdaptionContext<Tree> context = new AdaptionContext<>( adapter, new ScrollableControl<>( tree ) );
    StructuredScrollableHorizontalSelectionListener listener = new StructuredScrollableHorizontalSelectionListener( context );
    Point adapterLocation = shell.getLocation();
    Point location = tree.getLocation();

    listener.widgetSelected( createEvent( shell, SELECTION ) );

    assertThat( tree.getLocation().x ).isEqualTo( location.x - SELECTION - tree.getBorderWidth() );
    assertThat( tree.getLocation().y ).isEqualTo( location.y - tree.getBorderWidth() );
    assertThat( shell.getLocation() ).isEqualTo( adapterLocation );
  }

  private Composite reparentScrollable( Composite adapter, Scrollable scrollable ) {
    scrollable.setParent( adapter );
    new ControlReflectionUtil().setField( scrollable, "parent", shell );
    return adapter;
  }

  private void equipShellWithLayoutMargin() {
    FillLayout layout = new FillLayout( SWT.VERTICAL );
    layout.marginHeight = 10;
    layout.marginWidth = 10;
    shell.setLayout( layout );
    shell.layout();
  }
}