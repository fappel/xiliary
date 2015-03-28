package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.STUB_VERTICAL_BAR_OFFSET;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.stubContext;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Horizontal.H_INVISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Horizontal.H_VISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Vertical.V_INVISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Vertical.V_VISIBLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Horizontal;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Vertical;

public class ScrollableLayouterTest {

  private static final int SELECTION = 10;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollableLayouter layouter;
  private Composite adapter;
  private Shell shell;
  private Tree scrollable;

  @Before
  public void setUp() {
    shell = createShell( displayHelper );
    adapter = createAdapter( shell );
    scrollable = createTree( adapter, 6, 4 );
    layouter = new ScrollableLayouter( new LayoutContext<Tree>( adapter, scrollable ) );
    shell.open();
  }

  @Test
  public void withoutScrollBarsAndScrollableFitsInVisibleArea() {
    layouter.layout( stubLayoutContext( V_INVISIBLE, H_INVISIBLE, fitVisibleArea(), getVisibleArea() ) );

    assertThat( scrollable.getBounds() )
      .isEqualTo( getVisibleArea() );
  }

  @Test
  public void withoutScrollBarsAndScrollableDoesNotFitInVisibleArea() {
    layouter.layout( stubLayoutContext( V_INVISIBLE, H_INVISIBLE, exceedVisibleArea(), getVisibleArea() ) );

    assertThat( scrollable.getBounds() )
      .isEqualTo( $( 0, 0, exceedVisibleArea().x, getVisibleArea().height ) );
  }

  @Test
  public void withVerticalBarAndScrollableFitsInVisibleArea() {
    layouter.layout( stubLayoutContext( V_VISIBLE, H_INVISIBLE, fitVisibleArea(), getVisibleArea() ) );

    assertThat( scrollable.getBounds() )
      .isEqualTo( $( 0, 0, verticalHeightIfTreeFits(), getVisibleArea().height ) );
  }

  @Test
  public void withVerticalBarAndScrollableDoesNotFitInVisibleArea() {
    layouter.layout( stubLayoutContext( V_VISIBLE, H_INVISIBLE, exceedVisibleArea(), getVisibleArea() ) );

    assertThat( scrollable.getBounds() )
      .isEqualTo( $( 0, 0, verticalHeightIfTreeDoesNotFit(), getVisibleArea().height ) );
  }

  @Test
  public void withHorizontalBarAndScrollableFitsVisibleArea() {
    layouter.layout( stubLayoutContext( V_INVISIBLE, H_VISIBLE, fitVisibleArea(), getVisibleArea() ) );

    assertThat( scrollable.getBounds() )
      .isEqualTo( $( 0, 0, getVisibleArea().width, expectedVerticalHeightIfHorizontalIsVisible() ) );
  }

  @Test
  public void withHorizontalBarAndScrollableDoesNotFitVisibleArea() {
    layouter.layout( stubLayoutContext( V_INVISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea() ) );

    assertThat( scrollable.getBounds() )
      .isEqualTo( $( 0, 0, exceedVisibleArea().y, expectedVerticalHeightIfHorizontalIsVisible() ) );
  }

  @Test
  public void withScrollBarsAndScrollableDoesNotFitVisibleArea() {
    layouter.layout( stubLayoutContext( V_VISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea() ) );

    assertThat( scrollable.getBounds() )
      .isEqualTo( $( 0, 0, verticalHeightIfTreeDoesNotFit(), expectedVerticalHeightIfHorizontalIsVisible() ) );
  }

  @Test
  public void horizontalBarSelectionIfScrollableIsChildOfAdapter() {
    adapter.getHorizontalBar().setSelection( SELECTION );

    layouter.layout( stubLayoutContext( V_VISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea() ) );

    assertThat( scrollable.getBounds() )
      .isEqualTo( $( 0, 0, verticalHeightIfTreeDoesNotFit(), expectedVerticalHeightIfHorizontalIsVisible() ) );
  }

  @Test
  public void horizontalBarSelectionIfScrollableIsReplacedByAdapter() {
    expandTopBranch( scrollable );
    adapter.getHorizontalBar().setSelection( SELECTION );
    LayoutContext<?> context = stubLayoutContext( V_VISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea() );
    stubChildByAdapterReplacement( context );

    layouter.layout( context );

    assertThat( scrollable.getBounds() )
      .isEqualTo( $( -SELECTION, 0, verticalHeightIfTreeDoesNotFit(), expectedVerticalHeightIfHorizontalIsVisible() ) );
  }

  private LayoutContext<Scrollable> stubLayoutContext(
    Vertical verticalBarVisible, Horizontal horizontalBarVisible, Point preferredSize, Rectangle visibleArea )
  {
    LayoutContext<Scrollable> result
      = stubContext( verticalBarVisible, horizontalBarVisible, preferredSize, visibleArea );
    when( result.getScrollable() ).thenReturn( scrollable );
    when( result.getAdapter() ).thenReturn( adapter );
    return result;
  }

  private static void stubChildByAdapterReplacement( LayoutContext<?> context ) {
    when( context.isScrollableReplacedByAdapter() ).thenReturn( true );
    when( context.getHorizontalAdapterSelection() ).thenCallRealMethod();
  }

  private static Composite createAdapter( Composite parent ) {
    Composite result = new Composite( parent, SWT.H_SCROLL | SWT.V_SCROLL );
    result.setLayout( new FillLayout() );
    return result;
  }

  private int expectedVerticalHeightIfHorizontalIsVisible() {
    return adapter.getClientArea().height - BAR_BREADTH;
  }

  private int verticalHeightIfTreeFits() {
    return getVisibleArea().width + TreeLayoutContextHelper.STUB_VERTICAL_BAR_OFFSET;
  }

  private int verticalHeightIfTreeDoesNotFit() {
    return exceedVisibleArea().x + STUB_VERTICAL_BAR_OFFSET;
  }

  private Rectangle getVisibleArea() {
    return adapter.getClientArea();
  }

  private static Point fitVisibleArea() {
    return new Point( 0, 0 );
  }

  private Point exceedVisibleArea() {
    return new Point( adapter.getSize().x * 4, adapter.getSize().y * 4 );
  }

  private static Rectangle $( int x, int y, int width, int height ) {
    return new Rectangle( x, y, width, height );
  }
}