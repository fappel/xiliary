package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.openShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarAssert.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.testhelper.ShellHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;
import com.codeaffine.eclipse.swt.widget.scrollbar.Orientation;

public class VerticalScrollBarUpdaterTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Tree tree;
  private Shell shell;
  private FlatScrollBar scrollbar;
  private VerticalScrollBarUpdater updater;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    tree = createTree( shell, 6, 4 );
    scrollbar = new FlatScrollBar( shell, Orientation.VERTICAL );
    updater = new VerticalScrollBarUpdater( tree, scrollbar );
    openShell( shell );
  }

  @Test
  public void update() {
    expandRootLevelItems( tree );

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( 1 )
      .hasPageIncrement( expectedPageIncrement() )
      .hasThumb( expectedPageIncrement() )
      .hasMaximum( expectedMaximum() )
      .hasMinimum( 0 )
      .hasSelection( 0 );
  }

  @Test
  public void updateWithSelection() {
    expandTopBranch( tree );
    tree.setTopItem( tree.getItem( 0 ).getItem( 0 ) );

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( 1 )
      .hasPageIncrement( expectedPageIncrement() )
      .hasThumb( expectedPageIncrement() )
      .hasMaximum( expectedMaximum() )
      .hasMinimum( 0 )
      .hasSelection( 1 );
  }

  @Test
  public void updateWithGtkWorkaround() {
    adjustTreeHeightForGtkWorkaround();
    tree.setTopItem( tree.getItem( 1 ) );
    ShellHelper.waitForGtkRendering();

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( 1 )
      .hasPageIncrement( expectedPageIncrement() )
      .hasThumb( expectedPageIncrement() )
      .hasMaximum( expectedMaximum() )
      .hasMinimum( 0 )
      .hasSelection( 1 );
  }

  private void adjustTreeHeightForGtkWorkaround() {
    int treeHeight = tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).y - tree.getItemHeight() / 2;
    tree.setSize( tree.getSize().x, treeHeight );
  }

  private int expectedPageIncrement() {
    return tree.getClientArea().height / tree.getItemHeight();
  }

  private int expectedMaximum() {
    return new TreeItemCollector( tree ).collectVisibleItems().size();
  }
}