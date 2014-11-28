package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.stubContext;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Horizontal.H_INVISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Horizontal.H_VISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Vertical.V_INVISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Vertical.V_VISIBLE;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class ScrollableLayouterTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;
  private Tree tree;
  private ScrollableLayouter layouter;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    tree = createTree( shell, 6, 4 );
    layouter = new ScrollableLayouter( tree );
    shell.open();
  }

  @Test
  public void withoutScrollBarsAndTreeFitsInVisibleArea() {
    layouter.layout( stubContext( V_INVISIBLE, H_INVISIBLE, fitVisibleArea(), getVisibleArea() ) );

    assertThat( tree.getBounds() )
      .isEqualTo( getVisibleArea() );
  }

  @Test
  public void withoutScrollBarsAndTreeDoesNotFitInVisibleArea() {
    layouter.layout( stubContext( V_INVISIBLE, H_INVISIBLE, exceedVisibleArea(), getVisibleArea() ) );

    assertThat( tree.getBounds() )
      .isEqualTo( $( 0, 0, exceedVisibleArea().x, getVisibleArea().height ) );
  }

  @Test
  public void withVerticalBarAndTreeFitsInVisibleArea() {
    layouter.layout( stubContext( V_VISIBLE, H_INVISIBLE, fitVisibleArea(), getVisibleArea() ) );

    assertThat( tree.getBounds() )
      .isEqualTo( $( 0, 0, verticalHeightIfTreeFits(), getVisibleArea().height ) );
  }

  @Test
  public void withVerticalBarAndTreeDoesNotFitInVisibleArea() {
    layouter.layout( stubContext( V_VISIBLE, H_INVISIBLE, exceedVisibleArea(), getVisibleArea() ) );

    assertThat( tree.getBounds() )
      .isEqualTo( $( 0, 0, verticalHeightIfTreeDoesNotFit(), getVisibleArea().height ) );
  }

  @Test
  public void withHorizontalAndFitsVisibleArea() {
    layouter.layout( stubContext( V_INVISIBLE, H_VISIBLE, fitVisibleArea(), getVisibleArea() ) );

    assertThat( tree.getBounds() )
      .isEqualTo( $( 0, 0, getVisibleArea().width, expectedVerticalHeightIfHorizontalIsVisible() ) );
  }

  @Test
  public void withHorizontalAndDoesNotFitVisibleArea() {
    layouter.layout( stubContext( V_INVISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea() ) );

    assertThat( tree.getBounds() )
      .isEqualTo( $( 0, 0, exceedVisibleArea().y, expectedVerticalHeightIfHorizontalIsVisible() ) );
  }

  @Test
  public void withScrollBarsAndDoesNotFitVisibleArea() {
    layouter.layout( stubContext( V_VISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea() ) );

    assertThat( tree.getBounds() )
      .isEqualTo( $( 0, 0, verticalHeightIfTreeDoesNotFit(), expectedVerticalHeightIfHorizontalIsVisible() ) );
  }


  private int expectedVerticalHeightIfHorizontalIsVisible() {
    return shell.getClientArea().height - BAR_BREADTH;
  }

  private int verticalHeightIfTreeFits() {
    return getVisibleArea().width + TreeLayoutContextHelper.STUB_OFFSET;
  }

  private int verticalHeightIfTreeDoesNotFit() {
    return exceedVisibleArea().x + TreeLayoutContextHelper.STUB_OFFSET;
  }

  private Rectangle getVisibleArea() {
    return shell.getClientArea();
  }

  private static Point fitVisibleArea() {
    return new Point( 0, 0 );
  }

  private Point exceedVisibleArea() {
    return new Point( shell.getSize().x * 4, shell.getSize().y * 4 );
  }

  private static Rectangle $( int x, int y, int width, int height ) {
    return new Rectangle( x, y, width, height );
  }
}