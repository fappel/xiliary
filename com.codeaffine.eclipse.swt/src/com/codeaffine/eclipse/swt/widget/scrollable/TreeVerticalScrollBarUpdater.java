package com.codeaffine.eclipse.swt.widget.scrollable;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TreeVerticalScrollBarUpdater implements VerticalScrollBarUpdater {

  private final TreeItemCollector treeItemCollector;
  private final FlatScrollBar scrollBar;
  private final Tree tree;

  TreeVerticalScrollBarUpdater( Tree tree, FlatScrollBar scrollbar ) {
    this.treeItemCollector = new TreeItemCollector( tree );
    this.scrollBar = scrollbar;
    this.tree = tree;
  }

  @Override
  public void update() {
    List<TreeItem> visibleItems = treeItemCollector.collectVisibleItems();
    scrollBar.setIncrement( 1 );
    scrollBar.setMaximum( visibleItems.size() );
    scrollBar.setMinimum( 0 );
    scrollBar.setPageIncrement( calculateThumb() );
    scrollBar.setThumb( calculateThumb() );
    scrollBar.setSelection( calculateSelection( visibleItems ) );
  }

  int calculateThumb() {
    return tree.getClientArea().height / tree.getItemHeight();
  }

  private int calculateSelection( List<TreeItem> visibleItems ) {
    TreeItem topItem = tree.getTopItem();
    int result = calculateSelection( visibleItems.iterator(), topItem );
    return cornerCaseWorkaroundForGtk( result, topItem );
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
    return topItem.getBounds().y < 0 ? selection + 1 : selection;
  }
}