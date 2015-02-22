package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.LayoutContext.OVERLAY_OFFSET;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class LayoutContextTest {

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private LayoutContext<Tree> layoutContext;
  private Shell shell;
  private Tree tree;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    tree = createTree( shell, 2, 4 );
    layoutContext = new LayoutContext<Tree>( tree.getParent(), tree );
    shell.open();
  }

  @Test
  public void initial() {
    assertThat( layoutContext )
      .hasAdapter( shell )
      .hasScrollable( tree )
      .verticalBarIsInvisible()
      .horizontalBarIsInvisible()
      .hasPreferredSize( computePreferredTreeSize() )
      .hasVisibleArea( getVisibleArea() );
  }

  @Test
  public void newContext() {
    LayoutContext<Tree> context = layoutContext.newContext();

    assertThat( context )
      .hasAdapter( shell )
      .hasScrollable( tree )
      .verticalBarIsInvisible()
      .horizontalBarIsInvisible()
      .hasPreferredSize( computePreferredTreeSize() )
      .hasVisibleArea( getVisibleArea() );
  }

  @Test
  public void newContextWithItemHeight() {
    LayoutContext<Tree> context = layoutContext.newContext( tree.getItemHeight() );

    assertThat( context )
      .hasAdapter( shell )
      .hasScrollable( tree )
      .verticalBarIsInvisible()
      .horizontalBarIsInvisible()
      .hasPreferredSize( computePreferredTreeSize() )
      .hasVisibleArea( getVisibleArea() );
  }

  @Test
  public void preferredWidthExceedsVisibleAreaWidth() {
    shell.setSize( 200, 400 );
    expandTopBranch( tree );
    waitForGtkRendering();

    LayoutContext<Tree> context = layoutContext.newContext( tree.getItemHeight() );

    assertThat( context )
      .verticalBarIsInvisible()
      .horizontalBarIsVisible();
  }

  @Test
  public void preferredHeightExceedsVisibleAreaHeight() {
    shell.setSize( 200, 100 );
    expandRootLevelItems( tree );
    waitForGtkRendering();

    LayoutContext<Tree> context = layoutContext.newContext( tree.getItemHeight() );

    assertThat( context )
      .verticalBarIsVisible()
      .hasVerticalBarOffset( expectedVerticalBarOffset() )
      .horizontalBarIsInvisible();
  }

  @Test
  public void preferredSizeExceedsVisibleArea() {
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    LayoutContext<Tree> context = layoutContext.newContext( tree.getItemHeight() );

    assertThat( context )
      .verticalBarIsVisible()
      .hasVerticalBarOffset( expectedVerticalBarOffset() )
      .horizontalBarIsVisible();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class ) // Only for build server, works fine on Ubuntu
  public void verticalBarVisibilityOnThresholdHeightDependsOnHorizontalBarVisibility() {
    int thresholdHight = computeThresholdHeight();
    shell.setSize( 1000, thresholdHight );
    LayoutContext<Tree> first = layoutContext.newContext( tree.getItemHeight() );
    shell.setSize( 100, thresholdHight );
    LayoutContext<Tree> second = layoutContext.newContext( tree.getItemHeight() );

    assertThat( first )
      .verticalBarIsInvisible()
      .horizontalBarIsInvisible();
    assertThat( second )
      .verticalBarIsVisible()
      .horizontalBarIsVisible();
  }

  @Test
  public void getLocation() {
    LayoutContext<Tree> context = layoutContext.newContext( tree.getItemHeight() );

    Point actual = context.getLocation();

    assertThat( actual ).isEqualTo( expectedLocation() );
  }

  @Test
  public void getReconciliation() {
    LayoutContext<Tree> context = layoutContext.newContext( tree.getItemHeight() );

    Reconciliation first = layoutContext.getReconciliation();
    Reconciliation second = context.getReconciliation();

    assertThat( first ).isNotNull();
    assertThat( first ).isSameAs( second );
  }

  private int computeThresholdHeight() {
    int trim = shell.getSize().x - shell.getClientArea().height;
    return tree.getItemHeight() * 2  + trim + 3;
  }

  private Point computePreferredTreeSize() {
    Point size = tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
    return new Point( size.x + LayoutContext.WIDTH_BUFFER, size.y );
  }

  private Point expectedLocation() {
    return new Point( getVisibleArea().x, getVisibleArea().y );
  }

  private Rectangle getVisibleArea() {
    return shell.getClientArea();
  }

  private int expectedVerticalBarOffset() {
    int result = tree.getVerticalBar().getSize().x;
    if( result <= 0 ) {
      result = OVERLAY_OFFSET;
    }
    return result;
  }

  public static void waitForGtkRendering() {
    if( "gtk".equals( SWT.getPlatform() ) ) {
      long start = System.currentTimeMillis();
      while( ( System.currentTimeMillis() - start ) < 500 ) {
        if( !Display.getCurrent().readAndDispatch() ) {}
      }
    }
  }
}