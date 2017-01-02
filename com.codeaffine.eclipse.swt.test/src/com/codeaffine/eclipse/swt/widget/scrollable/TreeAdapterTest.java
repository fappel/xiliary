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

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.util.ReadAndDispatch;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class TreeAdapterTest {

  private static final int SELECTION = 50;

  @Rule public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollableAdapterFactory adapterFactory;
  private TreeAdapter adapter;
  private Shell shell;
  private Tree tree;

  private Object layoutData;

  @Before
  public void setUp() {
    adapterFactory = new ScrollableAdapterFactory();
    shell = createShell( displayHelper );
    tree = createTree( shell, 4, 6 );
    layoutData = new Object();
    tree.setLayoutData( layoutData );
    adapter = adapterFactory.create( tree, TreeAdapter.class ).get();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void adapt() {
    assertThat( adapter.getChildren() ).contains( tree );
    assertThat( adapter.getLayout() ).isInstanceOf( ScrollableLayout.class );
    assertThat( adapter.getLayoutData() ).isSameAs( layoutData );
    assertThat( adapter.getBounds() ).isEqualTo( shell.getClientArea() );
    assertThat( adapter.getScrollable() ).isSameAs( tree );
  }

  @Test
  public void setLayout() {
    Throwable actual = thrownBy( () -> adapter.setLayout( new FillLayout() ) );

    assertThat( actual ).isInstanceOf( UnsupportedOperationException.class );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void disposalOfAdapter() {
    adapter.dispose();

    assertThat( tree.isDisposed() ).isTrue();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void disposalOfTree() {
    tree.dispose();

    assertThat( adapter.isDisposed() ).isTrue();
    assertThat( shell.getChildren() ).isEmpty();
  }

  @Test
  public void disposalWithDragSourceOnTree() {
    Tree tree = new TestTreeFactory().create( shell );
    ScrollableAdapterFactoryHelper.adapt( tree, TreeAdapter.class );
    DragSource dragSource = new DragSource( tree, DND.DROP_MOVE | DND.DROP_COPY );

    shell.dispose();

    assertThat( dragSource.isDisposed() ).isTrue();
  }

  @Test
  public void constructor() {
    Throwable actual = thrownBy( () -> new TreeAdapter() );

    assertThat( actual )
      .hasMessageContaining( "Subclassing not allowed" )
      .isInstanceOf( SWTException.class );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTreeBounds() {
    openShellWithoutLayout();
    tree = createTree( shell, 1, 1 );
    adapter = adapterFactory.create( tree, TreeAdapter.class ).get();
    waitForReconciliation();

    tree.setBounds( expectedBounds() );
    waitForReconciliation();

    assertThat( adapter.getBounds() ).isEqualTo( expectedBounds() );
    assertThat( tree.getBounds() ).isEqualTo( expectedBounds() );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTreeBoundsWithVisibleScrollBars() {
    openShellWithoutLayout();
    expandTopBranch( tree );

    tree.setBounds( expectedBounds() );
    waitForReconciliation();

    assertThat( adapter.getBounds() ).isEqualTo( expectedBounds() );
    assertThat( tree.getBounds() ).isNotEqualTo( expectedBounds() );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTreeBoundsByTreeEvent() {
    openShellWithoutLayout();
    Rectangle expected = adapter.getBounds();
    Rectangle origin = tree.getBounds();

    expandTopBranch( tree );
    waitForReconciliation();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
    assertThat( tree.getBounds() ).isNotEqualTo( origin );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTreeBoundsWithHorizontalScroll() {
    openShellWithoutLayout();
    expandTopBranch( tree );

    tree.setBounds( expectedBounds() );
    waitForReconciliation();
    scrollHorizontal( adapter, SELECTION );

    assertThat( adapter.getBounds() ).isEqualTo( expectedBounds() );
    assertThat( tree.getBounds() ).isNotEqualTo( expectedBounds() );
    assertThat( adapter.getHorizontalBar().getSelection() ).isEqualTo( SELECTION );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTreeVisibility() {
    tree.setVisible( false );
    waitForReconciliation();

    assertThat( adapter.getVisible() ).isFalse();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTreeEnablement() {
    tree.setEnabled( false );
    waitForReconciliation();

    assertThat( adapter.getEnabled() ).isFalse();
  }

  @Test
  public void getLayoutData() {
    RowData expected = new RowData();
    tree.setLayoutData( expected );

    Object actual = adapter.getLayoutData();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void setLayoutData() {
    Object expected = new Object();

    adapter.setLayoutData( expected );
    Object actual = tree.getLayoutData();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void computeSize() {
    Point expected = tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );

    Point actual = adapter.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void treeColumnDelegation() {
    TreeColumn expected = new TreeColumn( tree, SWT.NONE );

    assertThat( adapter.getColumn( 0 ) ).isSameAs( expected );
    assertThat( adapter.getColumnCount() ).isEqualTo( 1 );
    assertThat( adapter.getColumnOrder() ).isEqualTo( tree.getColumnOrder() );
    assertThat( adapter.getColumns() ).hasSize( 1 );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void changeTreeItemHeight() {
    expandTopBranch( tree );
    int expectedHeight = configureTableItemHeightAdjuster();
    shell.open();
    waitForReconciliation();

    assertThat( tree.getItemHeight() ).isEqualTo( expectedHeight );
  }

  @Test
  public void adaptWithoutScrollBarStyle() {
    openShellWithoutLayout();
    tree = new Tree( shell, SWT.NO_SCROLL );
    Optional<TreeAdapter> adapter = adapterFactory.create( tree, TreeAdapter.class );

    assertThat( adapter.isPresent() ).isFalse();
  }

  private int configureTableItemHeightAdjuster() {
    int result = 24;
    tree.addListener( SWT.MeasureItem, evt -> evt.height = result );
    flushPendingEvents();
    return result;
  }

  private void scrollHorizontal( TreeAdapter adapter, int selection ) {
    int duration = 100;
    displayHelper.getDisplay().timerExec( duration, () -> adapter.getHorizontalBar().setSelection( selection ) );
    new ReadAndDispatch().spinLoop( shell, duration * 2 );
  }

  private void openShellWithoutLayout() {
    shell.setLayout( null );
    shell.open();
  }

  private void waitForReconciliation() {
    new ReadAndDispatch().spinLoop( shell, WatchDog.DELAY * 6 );
  }

  private Rectangle expectedBounds() {
    Rectangle clientArea = shell.getClientArea();
    return new Rectangle( 5, 10, clientArea.width - 10, clientArea.height - 20 );
  }
}