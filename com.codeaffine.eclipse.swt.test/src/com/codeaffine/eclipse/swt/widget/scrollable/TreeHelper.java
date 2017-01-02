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

import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

public class TreeHelper {

  public static void expandTopBranch( Tree tree ) {
    TreeItem item = tree.getItem( 0 );
    item.setExpanded( true );
    while( item.getItemCount() > 0 ) {
      item = item.getItem( 0 );
      item.setExpanded( true );
    }
    flushPendingEvents();
  }

  public static void expandRootLevelItems( Tree tree ) {
    Stream.of( tree.getItems() ).forEach( item -> item.setExpanded( true ) );
    flushPendingEvents();
  }

  static Tree createOwnerDrawnTreeWithColumn( Composite parent ) {
    Tree result = createTree( parent, 2, 6, SWT.FULL_SELECTION );
    result.setHeaderVisible( true );
    result.setLinesVisible( true );
    result.addListener( SWT.MeasureItem, evt -> {} );
    result.addListener( SWT.PaintItem, evt -> {} );
    result.addListener( SWT.MouseDown, evt -> {} );
    result.setFocus();

    TreeColumn labelColumn = new TreeColumn( result, SWT.NONE );
    labelColumn.setText( "Column" );
    labelColumn.setWidth( 200 );
    labelColumn.setResizable( true );
    return result;
  }

  public static Tree createTree( Composite parent, int childCount, int levelCount  ) {
    return createTree( parent, childCount, levelCount, SWT.NONE );
  }

  public static Tree createTree( Composite parent, int childCount, int levelCount, int style ) {
    Tree result = new Tree( parent, style );
    createChildren( result, "tree-item_", childCount, levelCount );
    return result;
  }

  static void createChildren( Widget parent, String name, int childCount, int levelCount  ) {
    for( int i = 0; i < childCount; i++ ) {
      TreeItem item = createItem( parent );
      String itemName = name + i;
      item.setText( itemName );
      if( levelCount > 0  ) {
        createChildren( item, itemName + "_" , childCount, levelCount - 1 );
      }
    }
  }

  private static TreeItem createItem( Widget parent ) {
    if( parent instanceof TreeItem ) {
      return new TreeItem( ( TreeItem )parent, SWT.NONE );
    }
    return new TreeItem( ( Tree )parent, SWT.NONE );
  }
}