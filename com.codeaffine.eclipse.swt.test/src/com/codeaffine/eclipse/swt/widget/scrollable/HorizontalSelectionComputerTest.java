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

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.CocoaPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;


public class HorizontalSelectionComputerTest {

  private static final int SELECTION = 4;

  @Rule
  public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private HorizontalSelectionComputer computer;
  private Tree scrollable;
  private Shell adapter;

  @Before
  public void setUp() {
    adapter = createShell( displayHelper, SWT.H_SCROLL | SWT.V_SCROLL );
    scrollable = createTree( adapter, 6, 4 );
    computer = new HorizontalSelectionComputer();
    adapter.open();
  }

  @Test
  public void compute() {
    AdaptionContext<Scrollable> context = new AdaptionContext<>( adapter, new ScrollableControl<>( scrollable ) );

    int actual = computer.compute( context );

    assertThat( actual ).isEqualTo( 0 );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void computeWithSelection() {
    expandTopBranch( scrollable );
    adapter.getHorizontalBar().setSelection( SELECTION );
    AdaptionContext<Scrollable> context = new AdaptionContext<>( adapter, new ScrollableControl<>( scrollable ) );

    int actual = computer.compute( context );

    assertThat( actual ).isEqualTo( 0 );
  }

  @Test
  public void computeWithAdapterReplacementFake() {
    Composite composite = new Composite( scrollable.getParent(), SWT.H_SCROLL | SWT.V_SCROLL );
    AdaptionContext<Scrollable> context = new AdaptionContext<>( composite, new ScrollableControl<>( scrollable ) );

    int actual = computer.compute( context );

    assertThat( actual ).isEqualTo( 0 );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void computeWithAdapterReplacementFakeAndSelection() {
    Composite composite = new Composite( scrollable.getParent(), SWT.H_SCROLL | SWT.V_SCROLL );
    composite.getHorizontalBar().setSelection( SELECTION );
    expandTopBranch( scrollable );
    AdaptionContext<Scrollable> context = new AdaptionContext<>( composite, new ScrollableControl<>( scrollable ) );

    int actual = computer.compute( context );

    assertThat( actual ).isEqualTo( SELECTION );
  }

  @Test
  public void computeWithAdapterReplacementFakeAndSelectionOnSmallScrollable() {
    Composite composite = new Composite( scrollable.getParent(), SWT.H_SCROLL | SWT.V_SCROLL );
    composite.getHorizontalBar().setSelection( SELECTION );
    scrollable.setSize( 0, 0 );
    AdaptionContext<Scrollable> context = new AdaptionContext<>( composite, new ScrollableControl<>( scrollable ) );

    int actual = computer.compute( context );

    assertThat( actual ).isEqualTo( 0 );
  }
}