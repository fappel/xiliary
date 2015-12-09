package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShellWithoutLayout;
import static com.codeaffine.eclipse.swt.widget.scrollable.AdaptionContextHelper.BORDER_WIDTH;
import static com.codeaffine.eclipse.swt.widget.scrollable.AdaptionContextHelper.stubContext;
import static com.codeaffine.eclipse.swt.widget.scrollable.AdaptionContextHelper.Horizontal.H_INVISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.AdaptionContextHelper.Horizontal.H_VISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.AdaptionContextHelper.Vertical.V_INVISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.AdaptionContextHelper.Vertical.V_VISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.MAX_EXPANSION;
import static com.codeaffine.eclipse.swt.widget.scrollable.OverlayLayouter.CORNER_COVERAGE;
import static com.codeaffine.eclipse.swt.widget.scrollable.OverlayLayouter.calculateCornerOverlayBounds;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarAssert.assertThat;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

@RunWith( Parameterized.class )
public class OverlayLayouterTest {

  private static final int OFFSET = 2;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private FlatScrollBar horizontal;
  private OverlayLayouter layouter;
  private FlatScrollBar vertical;
  private Label cornerOverlay;
  private Shell shell;

  @Parameter
  public Demeanor demeanor;

  @Parameters
  public static Collection<Demeanor> getDemeanor() {
    return asList( Demeanor.values() );
  }

  @Before
  public void setUp() {
    shell = createShellWithoutLayout( displayHelper, SWT.RESIZE );
    horizontal = new FlatScrollBar( shell, SWT.HORIZONTAL );
    vertical = new FlatScrollBar( shell, SWT.VERTICAL );
    cornerOverlay = new Label( shell, SWT.NONE );
    layouter = new OverlayLayouter( horizontal, vertical, cornerOverlay );
  }

  @Test
  public void layout() {
    AdaptionContext<?> context = stubContext( V_VISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea(), demeanor );

    layouter.layout( context );

    assertThat( horizontal )
      .isVisible()
      .hasBounds( 0, expectedHorizontalY( context ), getVisibleArea().width - MAX_EXPANSION, barBreadth( context ) );
    assertThat( vertical )
      .isVisible()
      .hasBounds( expectedVerticalX( context ), 0, barBreadth( context ), getVisibleArea().height - MAX_EXPANSION );
    assertThat( cornerOverlay.getBounds() )
      .isEqualTo( expectedCornerOverlayBounds() );
  }

  @Test
  public void layoutWithoutHorizontalBar() {
    AdaptionContext<?> context = stubContext( V_VISIBLE, H_INVISIBLE, exceedVisibleArea(), getVisibleArea(), demeanor );

    layouter.layout( context );

    assertThat( horizontal )
      .isNotVisible()
      .hasBounds( 0, 0, 0, 0 );
    assertThat( vertical )
      .isVisible()
      .hasBounds( expectedVerticalX( context ), 0, barBreadth( context ), getVisibleArea().height );
    assertThat( cornerOverlay.getBounds() )
      .isEqualTo( expectedCornerOverlayBounds() );
  }

  @Test
  public void layoutWithoutVerticalBar() {
    AdaptionContext<?> context = stubContext( V_INVISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea(), demeanor );

    layouter.layout( context );

    assertThat( horizontal )
      .isVisible()
      .hasBounds( 0, expectedHorizontalY( context ), getVisibleArea().width, barBreadth( context ) );
    assertThat( vertical )
      .isNotVisible()
      .hasBounds( 0, 0, 0, 0 );
    assertThat( cornerOverlay.getBounds() )
      .isEqualTo( expectedCornerOverlayBounds() );
  }

  @Test
  public void layoutWithoutScrollBars() {
    AdaptionContext<?> context = stubContext( V_INVISIBLE, H_INVISIBLE, fitVisibleArea(), getVisibleArea(), demeanor );

    layouter.layout( context );

    assertThat( horizontal )
      .isNotVisible()
      .hasBounds( 0, 0, 0, 0 );
    assertThat( vertical )
      .isNotVisible()
      .hasBounds( 0, 0, 0, 0 );
    assertThat( cornerOverlay.getBounds() )
      .isEqualTo( expectedCornerOverlayBounds() );
  }

  @Test
  public void cornerOverlayBoundsCalculation() {
    AdaptionContext<?> context = stubContextWithOffset( OFFSET );
    horizontal.setSize( 10, 20 );
    vertical.setSize( 30, 40 );

    Rectangle actual = OverlayLayouter.calculateCornerOverlayBounds( horizontal, vertical, context );

    int width = 30 + OFFSET + BORDER_WIDTH + CORNER_COVERAGE;
    int height = 20 + BORDER_WIDTH + CORNER_COVERAGE;
    assertThat( actual ).isEqualTo( new Rectangle( 10, 40, width, height) );
  }

  private AdaptionContext<?> stubContextWithOffset( int offset ) {
    AdaptionContext<?> result = stubContext( V_VISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea(), demeanor );
    when( result.getOffset() ).thenReturn( offset );
    return result;
  }

  private Rectangle expectedCornerOverlayBounds() {
    AdaptionContext<?> context = stubContext( V_VISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea(), demeanor );
    return calculateCornerOverlayBounds( horizontal, vertical , context);
  }

  private int expectedHorizontalY( AdaptionContext<?> context ) {
    return getVisibleArea().height - barBreadth( context );
  }

  private int expectedVerticalX( AdaptionContext<?> context ) {
    return getVisibleArea().width - barBreadth( context );
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

  private static int barBreadth( AdaptionContext<?> context ) {
    return context.get( Demeanor.class ).getBarBreadth();
  }
}