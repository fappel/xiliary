package com.codeaffine.eclipse.swt.widget.scrollable;

import java.util.List;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

class TreeTopItemSelector {

  private final TreeItemCollector collector;
  private final Tree tree;

  TreeTopItemSelector( Tree tree ) {
    this.collector = new TreeItemCollector( tree );
    this.tree = tree;
  }

  void select( int selection ) {
    List<TreeItem> items = collector.collectVisibleItems();
    TreeItem toSelect = items.get( selection );
    tree.setTopItem( toSelect );
  }
}