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

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class TreeTopItemSelectorTest {

  private static final int TEN_ITEMS_PER_LEVEL = 10;
  private static final int TWO_LEVELS = 2;
  private static final int INDEX_OF_SECOND_TOP_LEVEL_ITEM = 11;

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void select() {
    Tree tree = createTree( TEN_ITEMS_PER_LEVEL, TWO_LEVELS );
    expandFirstTopLevelItem( tree );
    TreeTopItemSelector selector = new TreeTopItemSelector( tree );

    selector.select( INDEX_OF_SECOND_TOP_LEVEL_ITEM );

    assertThat( tree.getTopItem() ).isSameAs( getSecondTopLevelItem( tree ) );
  }

  private Tree createTree( int tenItemsPerLevel, int levelCount ) {
    Shell shell = createShell( displayHelper );
    Tree result = TreeHelper.createTree( shell, tenItemsPerLevel, levelCount );
    shell.open();
    return result;
  }

  private static void expandFirstTopLevelItem( Tree tree ) {
    expandRootLevelItems( tree );
  }

  private static TreeItem getSecondTopLevelItem( Tree tree ) {
    return tree.getItem( 1 );
  }
}