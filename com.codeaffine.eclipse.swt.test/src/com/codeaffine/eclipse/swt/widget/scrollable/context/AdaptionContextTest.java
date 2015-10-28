package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandRootLevelItems;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext.OVERLAY_OFFSET;
import static com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContextAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.util.ControlReflectionUtil;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class AdaptionContextTest {

  private static final int SELECTION = 5;

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private AdaptionContext<Tree> adaptionContext;
  private Shell shell;
  private Tree tree;

  @Before
  public void setUp() {
    shell = createShell( displayHelper, SWT.RESIZE );
    tree = createTree( shell, 2, 4 );
    adaptionContext = new AdaptionContext<Tree>( tree.getParent(), tree );
    shell.open();
  }

  @Test
  public void initial() {
    assertThat( adaptionContext )
      .hasAdapter( shell )
      .hasScrollable( tree )
      .verticalBarIsInvisible()
      .horizontalBarIsInvisible()
      .hasPreferredSize( computePreferredTreeSize() )
      .hasOffset( new OffsetComputer( tree ).compute() )
      .hasVisibleArea( expectedVisibleArea( tree ) )
      .hasHorizontalAdapterSelection( 0 )
      .hasBorderWidth( expectedBorderWidth( tree ) )
      .isNotScrollableReplacement();
  }

  @Test
  public void newContext() {
    AdaptionContext<Tree> context = adaptionContext.newContext();

    assertThat( context )
      .hasAdapter( shell )
      .hasScrollable( tree )
      .verticalBarIsInvisible()
      .horizontalBarIsInvisible()
      .hasPreferredSize( computePreferredTreeSize() )
      .hasOffset( new OffsetComputer( tree ).compute() )
      .hasVisibleArea( expectedVisibleArea( tree ) )
      .hasHorizontalAdapterSelection( 0 )
      .hasBorderWidth( expectedBorderWidth( tree ) )
      .isNotScrollableReplacement();
  }

  @Test
  public void newContextWithItemHeight() {
    AdaptionContext<Tree> context = adaptionContext.newContext( tree.getItemHeight() );

    assertThat( context )
      .hasAdapter( shell )
      .hasScrollable( tree )
      .verticalBarIsInvisible()
      .horizontalBarIsInvisible()
      .hasPreferredSize( computePreferredTreeSize() )
      .hasOffset( new OffsetComputer( tree ).compute() )
      .hasVisibleArea( expectedVisibleArea( tree ) )
      .hasHorizontalAdapterSelection( 0 )
      .hasBorderWidth( expectedBorderWidth( tree ) )
      .isNotScrollableReplacement();
  }

  @Test
  public void preferredWidthExceedsVisibleAreaWidth() {
    shell.setSize( 200, 400 );
    expandTopBranch( tree );
    waitForGtkRendering();

    adaptionContext.updatePreferredSize();
    AdaptionContext<Tree> context = adaptionContext.newContext( tree.getItemHeight() );

    assertThat( context )
      .verticalBarIsInvisible()
      .horizontalBarIsVisible();
  }

  @Test
  public void preferredHeightExceedsVisibleAreaHeight() {
    shell.setSize( 200, 100 );
    expandRootLevelItems( tree );
    waitForGtkRendering();

    adaptionContext.updatePreferredSize();
    AdaptionContext<Tree> context = adaptionContext.newContext( tree.getItemHeight() );

    assertThat( context )
      .verticalBarIsVisible()
      .hasVerticalBarOffset( expectedVerticalBarOffset() )
      .horizontalBarIsInvisible();
  }

  @Test
  public void preferredSizeExceedsVisibleArea() {
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    adaptionContext.updatePreferredSize();
    AdaptionContext<Tree> context = adaptionContext.newContext( tree.getItemHeight() );

    assertThat( context )
      .verticalBarIsVisible()
      .hasVerticalBarOffset( expectedVerticalBarOffset() )
      .horizontalBarIsVisible();
  }

  @Test
  public void preferredSizeIsBuffered() {
    Point expected = adaptionContext.getPreferredSize();

    expandRootLevelItems( tree );
    expandTopBranch( tree );

    assertThat( adaptionContext ).hasPreferredSize( expected );
  }

  @Test
  public void updatePreferredSize() {
    Point initialSize = adaptionContext.getPreferredSize();
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    adaptionContext.updatePreferredSize();

    assertThat( adaptionContext ).hasNotPreferredSize( initialSize );
    assertThat( tree.getHorizontalBar().isVisible() ).isTrue();
  }

  @Test
  public void updatePreferredSizeIfIsOwnerDrawnAndVirtual() {
    tree.dispose();
    reinitWithOwnerDrawnAndVirtualScrollable();
    Point initialSize = adaptionContext.getPreferredSize();
    expandRootLevelItems( tree );
    expandTopBranch( tree );

    adaptionContext.updatePreferredSize();

    assertThat( adaptionContext ).hasNotPreferredSize( initialSize );
    assertThat( tree.getHorizontalBar().isVisible() ).isFalse();
  }

  @Test
  public void adjustPreferredWidthIfHorizontalBarIsVisible() {
    shell.setSize( 500, 400 );
    expandRootLevelItems( tree );
    expandTopBranch( tree );
    adaptionContext.updatePreferredSize();
    AdaptionContext<Tree> context = createContextWithReparentedScrollable();

    context.adjustPreferredWidthIfHorizontalBarIsVisible();
    tree.setSize( context.getPreferredSize() );
    AdaptionContext<Tree> actual = context.newContext( tree.getItemHeight() );

    assertThat( actual )
      .verticalBarIsInvisible()
      .hasVerticalBarOffset( expectedVerticalBarOffset() )
      .horizontalBarIsInvisible();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class ) // Only for build server, works fine on Ubuntu
  public void verticalBarVisibilityOnThresholdHeightDependsOnHorizontalBarVisibility() {
    int thresholdHight = computeThresholdHeight();
    shell.setSize( 1000, thresholdHight );
    AdaptionContext<Tree> first = adaptionContext.newContext( tree.getItemHeight() );
    shell.setSize( 90, thresholdHight );
    AdaptionContext<Tree> second = adaptionContext.newContext( tree.getItemHeight() );

    assertThat( first )
      .verticalBarIsInvisible()
      .horizontalBarIsInvisible();
    assertThat( second )
      .verticalBarIsVisible()
      .horizontalBarIsVisible();
  }

  @Test
  public void getOriginOfScrollableOrdinates() {
    AdaptionContext<Tree> actual = adaptionContext.newContext( tree.getItemHeight() );

    assertThat( actual ).hasOriginOfScrollableOrdinates( expectedOriginOfScrollableOrdinates( tree ) );
  }

  @Test
  public void getReconciliation() {
    AdaptionContext<Tree> context = adaptionContext.newContext( tree.getItemHeight() );

    Reconciliation first = adaptionContext.getReconciliation();
    Reconciliation second = context.getReconciliation();

    assertThat( first ).isNotNull();
    assertThat( first ).isSameAs( second );
  }

  @Test
  public void isScrollableAdaptedWithReplacementFake() {
    Composite composite = new Composite( tree.getParent(), SWT.NONE );

    AdaptionContext<Tree> actual = new AdaptionContext<Tree>( composite, tree );

    assertThat( actual ).isScrollableReplacement();
  }

  @Test
  public void getHorizontalAdapterWithReplacementFake() {
    Composite composite = new Composite( tree.getParent(), SWT.H_SCROLL | SWT.V_SCROLL );
    composite.getHorizontalBar().setSelection( SELECTION );

    AdaptionContext<Tree> actual = new AdaptionContext<Tree>( composite, tree );

    assertThat( actual ).hasHorizontalAdapterSelection( SELECTION );
  }

  @Test
  public void createContextOnScrollableWithBorder() {
    Tree scrollable = new Tree( shell, SWT.BORDER );
    AdaptionContext<Tree> actual = new AdaptionContext<Tree>( shell, scrollable );

    assertThat( actual )
      .hasOriginOfScrollableOrdinates( expectedOriginOfScrollableOrdinates( scrollable ) )
      .hasBorderWidth( scrollable.getBorderWidth() )
      .hasVisibleArea( expectedVisibleArea( scrollable ) );
  }

  private int computeThresholdHeight() {
    int trim = shell.getSize().x - shell.getClientArea().height;
    return tree.getItemHeight() * 2  + trim + 3;
  }

  private Point computePreferredTreeSize() {
    return new SizeComputer( tree, shell ).getPreferredSize();
  }

  private Point expectedOriginOfScrollableOrdinates( Tree scrollable ) {
    int borderWidth = expectedBorderWidth( scrollable );
    int x = expectedVisibleArea( scrollable ).x - borderWidth;
    int y = expectedVisibleArea( scrollable ).y - borderWidth;
    return new Point( x, y );
  }

  private Rectangle expectedVisibleArea( Tree scrollable ) {
    Rectangle area = shell.getClientArea();
    int borderAdjustment = expectedBorderWidth( scrollable ) * 2;
    return new Rectangle( area.x, area.y, area.width + borderAdjustment, area.height + borderAdjustment );
  }

  private int expectedVerticalBarOffset() {
    int result = tree.getVerticalBar().getSize().x;
    if( result <= 0 ) {
      result = OVERLAY_OFFSET;
    }
    return result;
  }

  private static int expectedBorderWidth( Tree scrollable ) {
    if( ( scrollable.getStyle() & SWT.BORDER ) > 0 ) {
      return scrollable.getBorderWidth();
    }
    return 0;
  }

  private AdaptionContext<Tree> createContextWithReparentedScrollable() {
    Composite parent = new Composite( shell, SWT.NONE );
    tree.setParent( parent );
    shell.setBounds( 100, 100, 1000, 700 );
    reparentScrollable( shell, tree );
    return new AdaptionContext<Tree>( parent, tree );
  }

  private static void reparentScrollable( Composite parent, Scrollable scrollable ) {
    new ControlReflectionUtil().setField( scrollable, "parent", parent );
  }

  private void reinitWithOwnerDrawnAndVirtualScrollable() {
    tree = createTree( shell, 2, 4, SWT.VIRTUAL );
    tree.addListener( SWT.MeasureItem, evt -> {} );
    adaptionContext = new AdaptionContext<Tree>( tree.getParent(), tree );
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