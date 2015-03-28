package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

class TreeHelper {

  static void expandTopBranch( Tree tree ) {
    TreeItem item = tree.getItem( 0 );
    item.setExpanded( true );
    while( item.getItemCount() > 0 ) {
      item = item.getItem( 0 );
      item.setExpanded( true );
    }
    flushPendingEvents();
  }

  static void expandRootLevelItems( Tree tree ) {
    TreeItem[] items = tree.getItems();
    for( TreeItem treeItem : items ) {
      treeItem.setExpanded( true );
    }
    flushPendingEvents();
  }

  static Tree createTree( Composite parent, int childCount, int levelCount  ) {
    return createTree( parent, childCount, levelCount, SWT.NONE );
  }

  static Tree createTree( Composite parent, int childCount, int levelCount, int style ) {
    Tree result = new Tree( parent, style );
    createChildren( result, "tree-item_", childCount, levelCount );
    return result;
  }

  private static void createChildren( Widget parent, String name, int childCount, int levelCount  ) {
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