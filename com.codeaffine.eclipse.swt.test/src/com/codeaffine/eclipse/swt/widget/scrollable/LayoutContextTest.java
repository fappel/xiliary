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
import org.eclipse.swt.widgets.Composite;
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

    layoutContext.updatePreferredSize();
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

    layoutContext.updatePreferredSize();
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

    layoutContext.updatePreferredSize();
    LayoutContext<Tree> context = layoutContext.newContext( tree.getItemHeight() );

    assertThat( context )
      .verticalBarIsVisible()
      .hasVerticalBarOffset( expectedVerticalBarOffset() )
      .horizontalBarIsVisible();
  }

  @Test
  public void preferredSizeIsBuffered() {
    Point expected = layoutContext.getPreferredSize();
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    Point actual = layoutContext.getPreferredSize();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void updatePreferredSize() {
    Point initialSize = layoutContext.getPreferredSize();
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    layoutContext.updatePreferredSize();
    Point actual = layoutContext.getPreferredSize();

    assertThat( actual ).isNotEqualTo( initialSize );
  }

  @Test
  public void adjustPreferredWidthIfHorizontalBarIsVisible() {
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    layoutContext.adjustPreferredWidthIfHorizontalBarIsVisible();
    LayoutContext<Tree> context = layoutContext.newContext( tree.getItemHeight() );

    assertThat( context )
      .verticalBarIsInvisible()
      .hasVerticalBarOffset( expectedVerticalBarOffset() )
      .horizontalBarIsInvisible();
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
  public void getOriginOfScrollabeOrdinates() {
    LayoutContext<Tree> context = layoutContext.newContext( tree.getItemHeight() );

    Point actual = context.getOriginOfScrollabeOrdinates();

    assertThat( actual ).isEqualTo( expectedOriginOfScrollableOrdinates() );
  }

  @Test
  public void getReconciliation() {
    LayoutContext<Tree> context = layoutContext.newContext( tree.getItemHeight() );

    Reconciliation first = layoutContext.getReconciliation();
    Reconciliation second = context.getReconciliation();

    assertThat( first ).isNotNull();
    assertThat( first ).isSameAs( second );
  }

  @Test
  public void getOffset() {
    int actual = layoutContext.getOffset();

    assertThat( actual ).isSameAs( new OffsetComputer( tree ).compute() );
  }

  @Test
  public void isScrollableReplacedByAdapter() {
    boolean actual = layoutContext.isScrollableReplacedByAdapter();

    assertThat( actual ).isFalse();
  }

  @Test
  public void isScrollableAdaptedWithReplacementFake() {
    Composite composite = new Composite( tree.getParent(), SWT.NONE );
    LayoutContext<Tree> context = new LayoutContext<Tree>( composite, tree );

    boolean actual = context.isScrollableReplacedByAdapter();

    assertThat( actual ).isTrue();
  }

  private int computeThresholdHeight() {
    int trim = shell.getSize().x - shell.getClientArea().height;
    return tree.getItemHeight() * 2  + trim + 3;
  }

  private Point computePreferredTreeSize() {
    return new PreferredSizeComputer( tree, shell ).getPreferredSize();
  }

  private Point expectedOriginOfScrollableOrdinates() {
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