package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.SelectionEventHelper.createEvent;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static com.codeaffine.eclipse.swt.widget.scrollable.VerticalScrollBarUpdater.SELECTION_RASTER_SMOOTH_FACTOR;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
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
    LayoutContext<Tree> context = new LayoutContext<Tree>( shell, tree );
    TreeVerticalSelectionListener listener = new TreeVerticalSelectionListener( context );

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
    VerticalScrollBarUpdater updater = new TreeVerticalScrollBarUpdater( tree, result );
    updater.update();
    return result;
  }

  private static TreeItem getThirdTopBranchItem( Tree tree ) {
    return tree.getItem( 0 ).getItem( 0 ).getItem( 0 );
  }
}