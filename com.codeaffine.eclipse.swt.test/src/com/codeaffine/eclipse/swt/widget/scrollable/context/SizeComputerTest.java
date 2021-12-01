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
package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.ScrolledCompositeHelper.createScrolledComposite;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.CocoaPlatform;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.NonWindowsPlatform;
import com.codeaffine.eclipse.swt.util.ControlReflectionUtil;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class SizeComputerTest {

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();
  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();

  private SizeComputer computer;
  private Composite adapter;
  private Tree scrollable;
  private Shell shell;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    adapter = createAdapter( shell );
    scrollable = createTree( adapter, 6, 4 );
    computer = createSizeComputer( scrollable, adapter );
    shell.layout();
    shell.open();
  }

  @Test
  public void getPreferredSizeIfWidthIsSmallerThanAdapterAreaWidth() {
    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expectedSizeWithClientAreaWidth() );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void getPreferredSizeIfWidthIsLargerThanAdapterAreaWidth() {
    expandTopBranch( scrollable );

    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expectedSize() );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void getPreferredSizeIfWidthIsLargerThanAdapterAreaWidthButHasOwnerDrawnItems() {
    expandTopBranch( scrollable );
    shell.setSize( 200, 200 );

    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expectedSize() );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void getPreferredSizeIfWidthIsLargerThanAdapterAreaWidthButHasOwnerDrawnItemsAndIsVirtual() {
    createOwnderDrawnVirtualScrollable();
    computer = createSizeComputer( scrollable, adapter );
    shell.layout();
    expandTopBranch( scrollable );
    shell.setSize( 200, 200 );

    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expectedSizeOnOwnerDrawnScrollable() );
    assertThat( scrollable.getHorizontalBar().isVisible() ).isFalse();
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class)
  public void getPreferredSizeOnIfWidthIsEqualsToScrollableContentWidth() {
    expandTopBranch( scrollable );
    shell.setSize( 200, 200 );
    Point expected = expectedSizeIfWidthIsEqualsToScrollableContentWidth();
    scrollable.setSize( expected );

    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void getPreferredSizeForScrolledComposite() {
    Stream.of( adapter.getChildren() ).forEach( child -> child.dispose() );
    ScrolledComposite scrolledComposite = createScrolledComposite( adapter );
    computer = createSizeComputer( scrolledComposite, adapter );

    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( scrolledComposite.getContent().getSize() );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void getPreferredSizeForScrolledCompositeThatHasNoContent() {
    Stream.of( adapter.getChildren() ).forEach( child -> child.dispose() );
    ScrolledComposite scrolledComposite = new ScrolledComposite( adapter, SWT.H_SCROLL | SWT.V_SCROLL );
    computer = createSizeComputer( scrolledComposite, adapter );

    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( scrolledComposite.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ) );
  }

  @Test
  public void preferredSizeGetsBuffered() {
    Point expected = computer.getPreferredSize();

    expandTopBranch( scrollable );
    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void adjustPreferredWidthIfHorizontalBarIsNotVisible() {
    computer.adjustPreferredWidthIfHorizontalBarIsVisible();
    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expectedSizeWithClientAreaWidth() );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void adjustPreferredWidthIfHorizontalBarIsVisible() {
    expandTopBranch( scrollable );

    computer.adjustPreferredWidthIfHorizontalBarIsVisible();
    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expectedSize() );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void adjustPreferredWidthIfHorizontalBarIsVisibleIfReparented() {
    reparentScrollable( shell, scrollable );
    expandTopBranch( scrollable );

    computer.adjustPreferredWidthIfHorizontalBarIsVisible();
    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( expectedSizeWithPreferredWidthAdjustment() );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void updatePreferredSize() {
    Point preferredSize = computer.getPreferredSize();
    expandTopBranch( scrollable );

    computer.updatePreferredSize();
    Point actual = computer.getPreferredSize();

    assertThat( actual ).isNotEqualTo( preferredSize );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class)
  public void updatePreferredSizeBuffering() {
    expandTopBranch( scrollable );
    shell.setSize( 400, 200 );

    computer.updatePreferredSize();
    Point buffered = computer.getPreferredSize();
    shell.setSize( 200, 200 );
    computer.updatePreferredSize();
    Point actual = computer.getPreferredSize();

    assertThat( actual ).isEqualTo( buffered );
  }

  private static Composite createAdapter( Composite parent ) {
    Composite result = new Composite( parent, SWT.NONE );
    result.setLayout( new FillLayout() );
    return result;
  }

  private Point expectedSizeWithClientAreaWidth() {
    return new Point( adapter.getClientArea().width, computePreferredScrollableSize().y );
  }

  private Point expectedSize() {
    ScrollBar horizontalBar = scrollable.getHorizontalBar();
    return new Point( horizontalBar.getMaximum(), computePreferredScrollableSize().y );
  }

  private Point expectedSizeWithPreferredWidthAdjustment() {
    ScrollBar horizontalBar = scrollable.getHorizontalBar();
    return new Point( horizontalBar.getMaximum() * 2, computePreferredScrollableSize().y );
  }

  private Point expectedSizeOnOwnerDrawnScrollable() {
    int height = scrollable.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).y;
    return new Point( adapter.getClientArea().width, height );
  }

  private Point expectedSizeIfWidthIsEqualsToScrollableContentWidth() {
    Point size = scrollable.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
    return new Point( size.x - scrollable.getVerticalBar().getSize().x, size.y );
  }

  private Point computePreferredScrollableSize() {
    return scrollable.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
  }

  private void createOwnderDrawnVirtualScrollable() {
    scrollable.dispose();
    scrollable = createTree( adapter, 6, 4, SWT.VIRTUAL );
    scrollable.addListener( SWT.MeasureItem, evt -> {} );
    scrollable.getHorizontalBar().setVisible( true );
  }

  private static void reparentScrollable( Composite parent, Scrollable scrollable ) {
    new ControlReflectionUtil().setField( scrollable, "parent", parent );
  }

  private static SizeComputer createSizeComputer( Scrollable scrollable, Composite adapter ) {
    return new SizeComputer( new ScrollableControl<>( scrollable ), adapter );
  }
}