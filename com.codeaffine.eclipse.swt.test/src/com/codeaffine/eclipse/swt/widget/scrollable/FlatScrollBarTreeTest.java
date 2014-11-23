package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.TreeFactory;

public class FlatScrollBarTreeTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private FlatScrollBarTree flatScrollBarTree;
  private Shell shell;
  private Tree tree;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    flatScrollBarTree = new FlatScrollBarTree( shell, new TreeFactory() {
      @Override
      public Tree create( Composite parent ) {
        tree = createTree( parent, 2, 6 );
        return tree;
      }
    } );
    shell.open();
  }

  @Test
  public void getTree() {
    Tree actual = flatScrollBarTree.getTree();

    assertThat( actual ).isSameAs( tree );
  }

  @Test
  public void getLayout() {
    Layout actual = flatScrollBarTree.getLayout();

    assertThat( actual ).isNotNull();
  }

  @Test( expected = UnsupportedOperationException.class )
  public void setLayout() {
    flatScrollBarTree.setLayout( new FillLayout() );
  }
}