package com.codeaffine.eclipse.swt.widget.scrollable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

class TreeItemCollector {

  private final Tree tree;

  TreeItemCollector( Tree tree ) {
    this.tree = tree;
  }

  List<TreeItem> collectVisibleItems() {
    TreeItem[] items = tree.getItems();
    return collectVisibleItems( items );
  }

  private List< TreeItem> collectVisibleItems( TreeItem[] treeItems ) {
    List<TreeItem> result = new ArrayList<TreeItem>();
    for( TreeItem treeItem : treeItems ) {
      result.add( treeItem );
      if( treeItem.getExpanded() ) {
        result.addAll( collectVisibleItems( treeItem.getItems() ) );
      }
    }
    return result;
  }
}