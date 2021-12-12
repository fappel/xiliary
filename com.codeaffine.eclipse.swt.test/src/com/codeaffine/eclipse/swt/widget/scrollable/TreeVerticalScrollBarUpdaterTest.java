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

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollBarUpdater.SELECTION_RASTER_SMOOTH_FACTOR;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.CocoaPlatform;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.NonWindowsPlatform;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class TreeVerticalScrollBarUpdaterTest {

  private static final int DEFAULT_MAXIMUM = 100;

  @Rule public final ConditionalIgnoreRule conditionIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private Tree tree;
  private Shell shell;
  private FlatScrollBar scrollbar;
  private TreeVerticalScrollBarUpdater updater;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    tree = createTree( shell, 6, 4 );
    scrollbar = new FlatScrollBar( shell, SWT.VERTICAL );
    updater = new TreeVerticalScrollBarUpdater( tree, scrollbar );
    shell.open();
  }

  @Test
  public void update() {
    expandRootLevelItems( tree );

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( SELECTION_RASTER_SMOOTH_FACTOR )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( updater.calculateThumb() )
      .hasMaximum( expectedMaximum() )
      .hasMinimum( 0 )
      .hasSelection( 0 );
  }

  @Test
  public void updateWithBuffering() {
    expandRootLevelItems( tree );

    updater.update();
    updater.update();

    assertThat( scrollbar )
    .hasIncrement( SELECTION_RASTER_SMOOTH_FACTOR )
    .hasPageIncrement( updater.calculateThumb() )
    .hasThumb( updater.calculateThumb() )
    .hasMaximum( expectedMaximum() )
    .hasMinimum( 0 )
    .hasSelection( 0 );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void updateWithSelection() {
    expandTopBranch( tree );
    tree.setTopItem( tree.getItem( 0 ).getItem( 0 ) );

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( SELECTION_RASTER_SMOOTH_FACTOR )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( updater.calculateThumb() )
      .hasMaximum( expectedMaximum() )
      .hasMinimum( 0 )
      .hasSelection( SELECTION_RASTER_SMOOTH_FACTOR );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void updateWithGtkWorkaround() {
    adjustTreeHeightForGtkWorkaround();
    tree.setTopItem( tree.getItem( 1 ) );
    flushPendingEvents();

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( SELECTION_RASTER_SMOOTH_FACTOR )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( updater.calculateThumb() )
      .hasMaximum( expectedMaximum() )
      .hasMinimum( 0 )
      .hasSelection( SELECTION_RASTER_SMOOTH_FACTOR );
  }

  @Test
  public void updateWithoutItems() {
    disposeAllItems();

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( SELECTION_RASTER_SMOOTH_FACTOR )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( DEFAULT_MAXIMUM )
      .hasMaximum( DEFAULT_MAXIMUM )
      .hasMinimum( 0 )
      .hasSelection( 0 );
  }

  @Test
  public void updateWithoutHeader() {
    expandRootLevelItems( tree );

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( SELECTION_RASTER_SMOOTH_FACTOR )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( updater.calculateThumb() )
      .hasMaximum( expectedMaximum() )
      .hasMinimum( 0 )
      .hasSelection( 0 );
  }

  @Test
  public void calculateThumb() {
    expandRootLevelItems( tree );
    updater.update();

    int before = updater.calculateThumb();
    tree.setHeaderVisible( true );
    int after = updater.calculateThumb();

    assertThat( before ).isNotEqualTo( after );
  }

  @Test
  public void ensureCorrectRounding() {
    expandRootLevelItems( tree );
    int height = tree.getClientArea().height;

    int actual = updater.calculateThumb();

    assertThat( actual ).isLessThan( SELECTION_RASTER_SMOOTH_FACTOR * height / tree.getItemHeight() );
  }

  private void adjustTreeHeightForGtkWorkaround() {
    int size = new TreeItemCollector( tree ).collectVisibleItems().size();
    int treeHeight = size * tree.getItemHeight() - tree.getItemHeight() / 2;
    tree.setSize( tree.getSize().x, treeHeight );
  }

  private int expectedMaximum() {
    int size = new TreeItemCollector( tree ).collectVisibleItems().size();
    return SELECTION_RASTER_SMOOTH_FACTOR * size;
  }

  private void disposeAllItems() {
    TreeItem[] items = tree.getItems();
    for( TreeItem treeItem : items ) {
      treeItem.dispose();
    }
  }
}