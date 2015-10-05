package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShellWithoutLayout;
import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.MAX_EXPANSION;
import static com.codeaffine.eclipse.swt.widget.scrollable.AdaptionContextHelper.BORDER_WIDTH;
import static com.codeaffine.eclipse.swt.widget.scrollable.AdaptionContextHelper.stubContext;
import static com.codeaffine.eclipse.swt.widget.scrollable.AdaptionContextHelper.Horizontal.H_INVISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.AdaptionContextHelper.Horizontal.H_VISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.AdaptionContextHelper.Vertical.V_INVISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.AdaptionContextHelper.Vertical.V_VISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.OverlayLayouter.calculateCornerOverlayBounds;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.BAR_BREADTH;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class OverlayLayouterTest {

  private static final int OFFSET = 2;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private FlatScrollBar horizontal;
  private OverlayLayouter layouter;
  private FlatScrollBar vertical;
  private Label cornerOverlay;
  private Shell shell;

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
    AdaptionContext<?> context = stubContext( V_VISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea() );

    layouter.layout( context );

    assertThat( horizontal )
      .isVisible()
      .hasBounds( 0, expectedHorizontalY(), getVisibleArea().width - MAX_EXPANSION, BAR_BREADTH );
    assertThat( vertical )
      .isVisible()
      .hasBounds( expectedVerticalX(), 0, BAR_BREADTH, getVisibleArea().height - MAX_EXPANSION );
    assertThat( cornerOverlay.getBounds() )
      .isEqualTo( expectedCornerOverlayBounds() );
  }

  @Test
  public void layoutWithoutHorizontalBar() {
    AdaptionContext<?> context = stubContext( V_VISIBLE, H_INVISIBLE, exceedVisibleArea(), getVisibleArea() );

    layouter.layout( context );

    assertThat( horizontal )
      .isNotVisible()
      .hasBounds( 0, 0, 0, 0 );
    assertThat( vertical )
      .isVisible()
      .hasBounds( expectedVerticalX(), 0, BAR_BREADTH, getVisibleArea().height );
    assertThat( cornerOverlay.getBounds() )
      .isEqualTo( expectedCornerOverlayBounds() );
  }

  @Test
  public void layoutWithoutVerticalBar() {
    AdaptionContext<?> context = stubContext( V_INVISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea() );

    layouter.layout( context );

    assertThat( horizontal )
      .isVisible()
      .hasBounds( 0, expectedHorizontalY(), getVisibleArea().width, BAR_BREADTH );
    assertThat( vertical )
      .isNotVisible()
      .hasBounds( 0, 0, 0, 0 );
    assertThat( cornerOverlay.getBounds() )
      .isEqualTo( expectedCornerOverlayBounds() );
  }

  @Test
  public void layoutWithoutScrollBars() {
    AdaptionContext<?> context = stubContext( V_INVISIBLE, H_INVISIBLE, fitVisibleArea(), getVisibleArea() );

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

    assertThat( actual )
      .isEqualTo( new Rectangle( 10, 40, 30 + OFFSET + BORDER_WIDTH, 20 + BORDER_WIDTH ) );
  }

  private AdaptionContext<?> stubContextWithOffset( int offset ) {
    AdaptionContext<?> result = stubContext( V_VISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea() );
    when( result.getOffset() ).thenReturn( offset );
    return result;
  }

  private Rectangle expectedCornerOverlayBounds() {
    AdaptionContext<?> context = stubContext( V_VISIBLE, H_VISIBLE, exceedVisibleArea(), getVisibleArea() );
    return calculateCornerOverlayBounds( horizontal, vertical , context);
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