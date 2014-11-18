package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    flatScrollBarTree = new FlatScrollBarTree( shell, new TreeFactory() {
      @Override
      public Tree create( Composite parent ) {
        return createTree( parent, 2, 6 );
      }
    } );
    shell.open();
  }

  @Test
  public void structureAndDrawingOrder() {
    Layout layout = flatScrollBarTree.getLayout();
    Control[] children = flatScrollBarTree.getChildren();

    assertThat( children ).hasSize( 3 );
    assertThat( children[ 0 ] ).isExactlyInstanceOf( Composite.class );
    assertThat( children[ 1 ] ).isExactlyInstanceOf( Composite.class );
    assertThat( children[ 2 ] ).isExactlyInstanceOf( Tree.class );
    assertThat( layout ).isInstanceOf( FlatScrollBarTreeLayout.class );
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

  @Test( expected = UnsupportedOperationException.class )
  public void setLayout() {
    flatScrollBarTree.setLayout( new FillLayout() );
  }
}