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