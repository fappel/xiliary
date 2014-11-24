package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.openShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class CustomContentTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Composite flatScrollBarTree;
  private CustomContent customContent;
  private Shell shell;
  private Tree tree;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    flatScrollBarTree = new Composite( shell, SWT.NONE );
    tree = createTree( flatScrollBarTree, 2, 6 );
    customContent = new CustomContent( flatScrollBarTree, tree );
    openShell( shell );
  }

  @Test
  public void structureAndDrawingOrder() {
    Control[] children = flatScrollBarTree.getChildren();

    assertThat( children ).hasSize( 3 );
    assertThat( children[ 0 ] ).isSameAs( tree );
    assertThat( children[ 1 ] ).isExactlyInstanceOf( FlatScrollBar.class );
    assertThat( children[ 2 ] ).isExactlyInstanceOf( FlatScrollBar.class );
  }

  @Test
  public void background() {
    Control[] children = flatScrollBarTree.getChildren();

    assertThat( children[ 0 ].getBackground() ).isEqualTo( flatScrollBarTree.getBackground() );
    assertThat( children[ 1 ].getBackground() ).isEqualTo( flatScrollBarTree.getBackground() );
    assertThat( children[ 2 ].getBackground() ).isEqualTo( flatScrollBarTree.getBackground() );
  }

  @Test
  public void dispose() {
    flatScrollBarTree.dispose();

    assertThat( shell.getChildren() ).isEmpty();
  }

  @Test
  public void getLayout() {
    Layout actual = customContent.getLayout();

    assertThat( actual ).isInstanceOf( FlatScrollBarTreeLayout.class );
  }
}