package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
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
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.CocoaPlatform;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class TreeVerticalScrollBarUpdaterTest {

  @Rule public final ConditionalIgnoreRule conditionIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private Tree tree;
  private Shell shell;
  private FlatScrollBar scrollbar;
  private TreeVerticalScrollBarUpdater updater;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    tree = createTree( shell, 6, 4 );
    scrollbar = new FlatScrollBar( shell, SWT.VERTICAL );
    updater = new TreeVerticalScrollBarUpdater( tree, scrollbar );
    shell.open();
  }

  @Test
  public void update() {
    expandRootLevelItems( tree );

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( 1 )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( updater.calculateThumb() )
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
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( updater.calculateThumb() )
      .hasMaximum( expectedMaximum() )
      .hasMinimum( 0 )
      .hasSelection( 1 );
  }

  @Test
  @ConditionalIgnore( condition = CocoaPlatform.class )
  public void updateWithGtkWorkaround() {
    adjustTreeHeightForGtkWorkaround();
    tree.setTopItem( tree.getItem( 1 ) );
    flushPendingEvents();

    updater.update();

    assertThat( scrollbar )
      .hasIncrement( 1 )
      .hasPageIncrement( updater.calculateThumb() )
      .hasThumb( updater.calculateThumb() )
      .hasMaximum( expectedMaximum() )
      .hasMinimum( 0 )
      .hasSelection( 1 );
  }

  private void adjustTreeHeightForGtkWorkaround() {
    int treeHeight = expectedMaximum() * tree.getItemHeight() - tree.getItemHeight() / 2;
    tree.setSize( tree.getSize().x, treeHeight );
  }

  private int expectedMaximum() {
    return new TreeItemCollector( tree ).collectVisibleItems().size();
  }
}