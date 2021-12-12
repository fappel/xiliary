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