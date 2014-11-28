package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.SelectionEventHelper.createEvent;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
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

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void selectionChanged() {
    Shell shell = createShell( displayHelper );
    Tree tree = createTreeWithExpandedTopBranch( shell );
    FlatScrollBar scrollBar = prepareScrollBar( shell, tree );
    TreeVerticalSelectionListener listener = new TreeVerticalSelectionListener( tree );

    listener.widgetSelected( createEvent( scrollBar, 2 ) );

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
    TreeVerticalScrollBarUpdater updater = new TreeVerticalScrollBarUpdater( tree, result );
    updater.update();
    return result;
  }

  private static TreeItem getThirdTopBranchItem( Tree tree ) {
    return tree.getItem( 0 ).getItem( 0 ).getItem( 0 );
  }
}