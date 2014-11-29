package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class TreeItemCollectorTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private TreeItemCollector collector;
  private Shell shell;
  private Tree tree;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    tree = createTree( shell, 2, 2 );
    collector = new TreeItemCollector( tree );
  }

  @Test
  public void collectVisibleItems() {
    List<TreeItem> actual = collector.collectVisibleItems();

    assertThat( actual ).containsExactly( tree.getItems() );
  }

  @Test
  public void collectVisibleItemsIfFirstBranchIsExpanded() {
    expandTopBranch();

    List<TreeItem> actual = collector.collectVisibleItems();

    assertThat( actual ).containsExactly( getVisibleItemsIfTopBranchIsExpanded());
  }

  private void expandTopBranch() {
    tree.getItem( 0 ).setExpanded( true );
    tree.getItem( 0 ).getItem( 0 ).setExpanded( true );
  }

  private TreeItem[] getVisibleItemsIfTopBranchIsExpanded() {
    return new TreeItem[] {
      tree.getItem( 0 ),
      tree.getItem( 0 ).getItem( 0 ),
      tree.getItem( 0 ).getItem( 0 ).getItem( 0 ),
      tree.getItem( 0 ).getItem( 0 ).getItem( 1 ),
      tree.getItem( 0 ).getItem( 1 ),
      tree.getItem( 1 ) };
  }
}