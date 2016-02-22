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

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TreeVerticalScrollBarUpdater implements ScrollBarUpdater {

  private final VerticalScrollbarConfigurationBuffer scrollbarConfiguration;
  private final TreeItemCollector treeItemCollector;
  private final FlatScrollBar scrollBar;
  private final Tree tree;

  TreeVerticalScrollBarUpdater( Tree tree, FlatScrollBar scrollbar ) {
    this.scrollbarConfiguration = new VerticalScrollbarConfigurationBuffer( new ScrollableControl<>( tree ) );
    this.treeItemCollector = new TreeItemCollector( tree );
    this.scrollBar = scrollbar;
    this.tree = tree;
  }

  @Override
  public void update() {
    if( scrollbarConfiguration.hasChanged() ) {
      updateScrollbar();
    }
    scrollbarConfiguration.update();
  }

  private void updateScrollbar() {
    List<TreeItem> visibleItems = treeItemCollector.collectVisibleItems();
    scrollBar.setIncrement( SELECTION_RASTER_SMOOTH_FACTOR );
    scrollBar.setMaximum( calculateMaximum( visibleItems ) );
    scrollBar.setMinimum( 0 );
    scrollBar.setPageIncrement( calculateThumb() );
    scrollBar.setThumb( calculateThumb() );
    scrollBar.setSelection( calculateSelection( visibleItems ) );
  }

  int calculateMaximum( List<TreeItem> visibleItems ) {
    return SELECTION_RASTER_SMOOTH_FACTOR * visibleItems.size();
  }

  int calculateThumb() {
    int height = calculateHeight();
    int ratio = height / tree.getItemHeight();
    return SELECTION_RASTER_SMOOTH_FACTOR * ratio;
  }

  int calculateHeight() {
    int result = tree.getClientArea().height;
    if( tree.getHeaderVisible() ) {
      result -= tree.getHeaderHeight();
    }
    return result;
  }


  private int calculateSelection( List<TreeItem> visibleItems ) {
    TreeItem topItem = tree.getTopItem();
    int result = calculateSelection( visibleItems.iterator(), topItem );
    return SELECTION_RASTER_SMOOTH_FACTOR * cornerCaseWorkaroundForGtk( result, topItem );
  }

  private static int calculateSelection( Iterator<TreeItem> iterator, TreeItem topItem ) {
    int result = 0;
    while( checkNext( topItem, iterator ) ){
      result++;
    }
    return result;
  }

  private static boolean checkNext( TreeItem topItem, Iterator<TreeItem> iterator ) {
    return iterator.hasNext() && iterator.next() != topItem;
  }

  // [fappel]: setting topItem does not work reliable on gtk for last selection items
  // if top item is only half visible. The tree is moved correctly but top item returns the old value.
  // This recognizes such a situation and increases the flat scrollbar selection anyway.
  private static int cornerCaseWorkaroundForGtk( int selection, TreeItem topItem ) {
    return topItem != null && topItem.getBounds().y < 0 ? selection + 1 : selection;
  }
}