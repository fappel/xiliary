package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.ScrollEvent;

public class VerticalSelectionListenerTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void selectionChanged() {
    Shell shell = createShell( displayHelper );
    Tree tree = createTree( shell, 6, 4 );
    expandTopBranch( tree );
    VerticalSelectionListener listener = new VerticalSelectionListener( tree );

    listener.selectionChanged( new ScrollEvent( null, 2 ) );

    assertThat( tree.getTopItem() ).isSameAs( tree.getItem( 0 ).getItem( 0 ).getItem( 0 ) );
  }
}
