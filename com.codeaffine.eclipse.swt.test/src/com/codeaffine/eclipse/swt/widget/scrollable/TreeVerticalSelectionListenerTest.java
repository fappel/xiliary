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

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollBarUpdater.SELECTION_RASTER_SMOOTH_FACTOR;
import static com.codeaffine.eclipse.swt.widget.scrollable.SelectionEventHelper.createEvent;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class TreeVerticalSelectionListenerTest {

  private static final int ITEM_INDEX = 2;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void selectionChanged() {
    Shell shell = createShell( displayHelper );
    Tree tree = createTreeWithExpandedTopBranch( shell );
    FlatScrollBar scrollBar = prepareScrollBar( shell, tree );
    SelectionListener listener = new TreeVerticalSelectionListener( tree );

    listener.widgetSelected( createEvent( scrollBar, ITEM_INDEX * SELECTION_RASTER_SMOOTH_FACTOR ) );

    assertThat( tree.getTopItem() ).isSameAs( getThirdTopBranchItem( tree ) );
  }

  private static Tree createTreeWithExpandedTopBranch( Shell shell ) {
    Tree result = createTree( shell, 6, 4 );
    shell.open();
    expandTopBranch( result );
    return result;
  }

  private static FlatScrollBar prepareScrollBar( Shell shell, Tree tree ) {
    FlatScrollBar result = new FlatScrollBar( shell, SWT.VERTICAL );
    ScrollBarUpdater updater = new TreeVerticalScrollBarUpdater( tree, result );
    updater.update();
    return result;
  }

  private static TreeItem getThirdTopBranchItem( Tree tree ) {
    return tree.getItem( 0 ).getItem( 0 ).getItem( 0 );
  }
}