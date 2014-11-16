package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextAssert.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.testhelper.ShellHelper;

public class TreeLayoutContextTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;
  private Tree tree;

  @Before
  public void setUp() {
    shell = ShellHelper.createShell( displayHelper, SWT.RESIZE );
    tree = TreeHelper.createTree( shell, 2, 4 );
    shell.open();
  }

  @Test
  public void initial() {
    TreeLayoutContext context = new TreeLayoutContext( tree );

    assertThat( context )
      .verticalBarIsInvisible()
      .horizontalBarIsInvisible()
      .hasPreferredSize( computePreferredTreeSize() )
      .hasVisibleArea( getVisibleArea() );
  }

  @Test
  public void preferredWidthExceedsVisibleAreaWidth() {
    shell.setSize( 200, 400 );
    expandTopBranch( tree );

    TreeLayoutContext context = new TreeLayoutContext( tree );

    assertThat( context )
      .verticalBarIsInvisible()
      .horizontalBarIsVisible();
  }

  @Test
  public void preferredWidthExceedsVisibleAreaHeight() {
    shell.setSize( 200, 100 );
    expandRootLevelItems( tree );


    TreeLayoutContext context = new TreeLayoutContext( tree );

    assertThat( context )
      .verticalBarIsVisible()
      .horizontalBarIsInvisible();
  }

  @Test
  public void preferredSizeExceedsVisibleArea() {
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    TreeLayoutContext context = new TreeLayoutContext( tree );

    assertThat( context )
      .verticalBarIsVisible()
      .horizontalBarIsVisible();
  }

  private Point computePreferredTreeSize() {
    return tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
  }

  private Rectangle getVisibleArea() {
    return shell.getClientArea();
  }
}