package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.stubContext;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Horizontal.H_INVISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Horizontal.H_VISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Vertical.V_INVISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Vertical.V_VISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarAssert.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.testhelper.ShellHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;
import com.codeaffine.eclipse.swt.widget.scrollbar.Orientation;

public class TreeOverlayLayouterTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;
  private FlatScrollBar horizontal;
  private FlatScrollBar vertical;
  private TreeOverlayLayouter layouter;

  @Before
  public void setUp() {
    shell = ShellHelper.createShellWithoutLayout( displayHelper, SWT.RESIZE );
    horizontal = new FlatScrollBar( shell, Orientation.HORIZONTAL );
    vertical = new FlatScrollBar( shell, Orientation.VERTICAL );
    layouter = new TreeOverlayLayouter( horizontal, vertical );
  }

  @Test
  public void layout() {
    TreeLayoutContext context = stubContext( V_VISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea() );

    layouter.layout( context );

    assertThat( horizontal )
      .isVisible()
      .hasBounds( 0, expectedHorizontalY(), getVisibleArea().width - BAR_BREADTH, BAR_BREADTH );
    assertThat( vertical )
      .isVisible()
      .hasBounds( expectedVerticalX(), 0, BAR_BREADTH, getVisibleArea().height - BAR_BREADTH );
  }

  @Test
  public void layoutWithoutHorizontalBar() {
    TreeLayoutContext context = stubContext( V_VISIBLE, H_INVISIBLE, exceedVisibleArea(), getVisibleArea() );

    layouter.layout( context );

    assertThat( horizontal )
      .isNotVisible()
      .hasBounds( 0, 0, 0, 0 );
    assertThat( vertical )
      .isVisible()
      .hasBounds( expectedVerticalX(), 0, BAR_BREADTH, getVisibleArea().height );
  }

  @Test
  public void layoutWithoutVerticalBar() {
    TreeLayoutContext context = stubContext( V_INVISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea() );

    layouter.layout( context );

    assertThat( horizontal )
      .isVisible()
      .hasBounds( 0, expectedHorizontalY(), getVisibleArea().width, BAR_BREADTH );
    assertThat( vertical )
      .isNotVisible()
      .hasBounds( 0, 0, 0, 0 );
  }

  @Test
  public void layoutWithoutScrollBars() {
    TreeLayoutContext context = stubContext( V_INVISIBLE, H_INVISIBLE, fitVisibleArea(), getVisibleArea() );

    layouter.layout( context );

    assertThat( horizontal )
      .isNotVisible()
      .hasBounds( 0, 0, 0, 0 );
    assertThat( vertical )
      .isNotVisible()
      .hasBounds( 0, 0, 0, 0 );
  }

  private int expectedHorizontalY() {
    return getVisibleArea().height - BAR_BREADTH;
  }

  private int expectedVerticalX() {
    return getVisibleArea().width - BAR_BREADTH;
  }

  private Rectangle getVisibleArea() {
    return shell.getClientArea();
  }

  private Point exceedVisibleArea() {
    return new Point( shell.getSize().x * 2, shell.getSize().y * 2 );
  }

  private static Point fitVisibleArea() {
    return new Point( 0, 0 );
  }
}