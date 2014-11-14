package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.widget.scrollbar.ComponentDistribution.BUTTON_LENGTH;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_MAXIMUM;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_THUMB;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarAssert.assertThat;
import static com.codeaffine.eclipse.swt.widget.scrollbar.Orientation.BAR_BREADTH;
import static com.codeaffine.eclipse.swt.widget.scrollbar.Orientation.CLEARANCE;
import static com.codeaffine.eclipse.swt.widget.scrollbar.Orientation.HORIZONTAL;
import static com.codeaffine.eclipse.swt.widget.scrollbar.Orientation.MAX_EXPAND;
import static com.codeaffine.eclipse.swt.widget.scrollbar.Orientation.VERTICAL;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class OrientationTest {

  private static final int SELECTION = 12;

  @Rule
  public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private Shell shell;

  @Before
  public void setUp() {
    shell = displayHelper.createShell( SWT.RESIZE );
    shell.setLayout( new FillLayout( SWT.HORIZONTAL ) );
    shell.setBounds( 200, 200, 475, 475 );
    shell.open();
  }

  @Test
  public void layoutHorizontal() {
    FlatScrollBar scrollBar = createScrollBar( HORIZONTAL, SELECTION );

    shell.layout();

    ComponentDistribution distribution = getExpectedHorizontalDistribution( scrollBar, SELECTION );
    int height = getExpectedHeight( scrollBar );
    assertThat( scrollBar )
      .hasUpBounds( 0, CLEARANCE, BUTTON_LENGTH, height )
      .hasUpFastBounds( BUTTON_LENGTH, CLEARANCE, distribution.upFastLength, height )
      .hasDragBounds( distribution.dragStart, CLEARANCE, distribution.dragLength, height )
      .hasDownFastBounds( distribution.downFastStart, CLEARANCE, distribution.downFastLength, height )
      .hasDownBounds( distribution.downStart, CLEARANCE, BUTTON_LENGTH, height );
  }

  @Test
  public void layoutHorizontalWithUndercutOfThreeTimesButtonLength() {
    shell.setSize( BUTTON_LENGTH * 3, 475 );
    FlatScrollBar scrollBar = createScrollBar( HORIZONTAL, SELECTION );

    shell.layout();

    ComponentDistribution distribution = getExpectedHorizontalDistribution( scrollBar, SELECTION );
    int height = getExpectedHeight( scrollBar );
    assertThat( scrollBar )
      .hasUpBounds( 0, CLEARANCE, BUTTON_LENGTH, height )
      .hasDragBounds( 0, 0, 0, 0 )
      .hasDownBounds( distribution.downStart, CLEARANCE, BUTTON_LENGTH, height );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void layoutHorizontalWithUndercutOfTwoTimesButtonLength() {
    shell.setSize( BUTTON_LENGTH * 2, 475 );
    FlatScrollBar scrollBar = createScrollBar( HORIZONTAL, SELECTION );

    shell.layout();

    int height = getExpectedHeight( scrollBar );
    int halfWidth = scrollBar.getControl().getSize().x / 2;
    assertThat( scrollBar )
      .hasUpBounds( 0, CLEARANCE, halfWidth, height )
      .hasDragBounds( 0, 0, 0, 0 )
      .hasDownBounds( halfWidth, CLEARANCE, halfWidth, height );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void layoutHorizontalWithMaximumSelection() {
    FlatScrollBar scrollBar = createScrollBar( HORIZONTAL, DEFAULT_MAXIMUM );

    shell.layout();

    ComponentDistribution distribution = getExpectedHorizontalDistribution( scrollBar, scrollBar.getSelection() );
    int height = getExpectedHeight( scrollBar );
    assertThat( scrollBar )
      .hasUpBounds( 0, CLEARANCE, BUTTON_LENGTH, height )
      .hasUpFastBounds( BUTTON_LENGTH, CLEARANCE, distribution.upFastLength, height )
      .hasDragBounds( distribution.dragStart, CLEARANCE, distribution.dragLength, height )
      .hasDownFastBounds( distribution.downFastStart, CLEARANCE, distribution.downFastLength, height )
      .hasDownBounds( distribution.downStart, CLEARANCE, BUTTON_LENGTH, height );
  }

  @Test
  public void layoutHorizontalWithMaximumSelectionAndDragLengthRounding() {
    shell.setSize( 500, 500 );
    FlatScrollBar scrollBar = createScrollBar( HORIZONTAL, DEFAULT_MAXIMUM );

    shell.layout();

    ComponentDistribution distribution = getExpectedHorizontalDistribution( scrollBar, scrollBar.getSelection() );
    int height = getExpectedHeight( scrollBar );
    assertThat( scrollBar )
      .hasDragBounds( distribution.dragStart, CLEARANCE, distribution.dragLength + 1, height )
      .hasDownFastBounds( distribution.downFastStart, CLEARANCE, distribution.downFastLength - 1, height );
  }

  @Test
  public void setDefaultSizeHorizontal() {
    Point initialSize = new Point( 5, BAR_BREADTH + 2 );
    Control control = createControl( initialSize );

    HORIZONTAL.setDefaultSize( control );

    assertThat( control.getSize() )
      .isEqualTo( new Point( initialSize.x, BAR_BREADTH ) );
  }

  @Test
  public void computeSizeHorizontal() {
    int wHint = 10;

    Point actual = HORIZONTAL.computeSize( null, wHint, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( new Point( wHint, BAR_BREADTH ) );
  }

  @Test
  public void computeSizeHorizontalIfWHintIsDefault() {
    Rectangle clientArea = shell.getClientArea();
    Composite composite = new Composite( shell, SWT.NONE );

    Point actual = HORIZONTAL.computeSize( composite, SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( new Point( clientArea.width, BAR_BREADTH ) );
  }

  @Test
  public void expandMaximumHorizontal() {
    createDrawingOrderPredecessorControl();
    Rectangle bounds = new Rectangle( 4, 10, 40, 0 );
    Control control = createControl( bounds );

    HORIZONTAL.expand( control );

    assertTopOfDrawingOrder( control );
    assertThat( control.getBounds() )
      .isEqualTo( new Rectangle( bounds.x, bounds.y - MAX_EXPAND, bounds.width, bounds.height + MAX_EXPAND ) );
  }

  @Test
  public void expandHorizontalMinimum() {
    createDrawingOrderPredecessorControl();
    Rectangle bounds = new Rectangle( 4, 10, 40, 10 );
    Control control = createControl( bounds );

    HORIZONTAL.expand( control );

    assertTopOfDrawingOrder( control );
    assertThat( control.getBounds() )
      .isEqualTo( new Rectangle( bounds.x, bounds.y, bounds.width, bounds.height ) );
  }

  @Test
  public void layoutVertical() {
    FlatScrollBar scrollBar = createScrollBar( VERTICAL, SELECTION );

    shell.layout();

    ComponentDistribution distribution = getExpectedVerticalDistribution( scrollBar, SELECTION );
    int width = getExpectedWidth( scrollBar );
    assertThat( scrollBar )
      .hasUpBounds( CLEARANCE, 0, width, BUTTON_LENGTH )
      .hasUpFastBounds( CLEARANCE, BUTTON_LENGTH, width, distribution.upFastLength )
      .hasDragBounds( CLEARANCE, distribution.dragStart, width, distribution.dragLength )
      .hasDownFastBounds( CLEARANCE, distribution.downFastStart, width, distribution.downFastLength )
      .hasDownBounds( CLEARANCE, distribution.downStart, width, BUTTON_LENGTH );
  }

  @Test
  public void layoutVerticalWithUndercutOfThreeTimesButtonLength() {
    shell.setSize( 475, BUTTON_LENGTH * 3 );
    FlatScrollBar scrollBar = createScrollBar( VERTICAL, SELECTION );

    shell.layout();

    ComponentDistribution distribution = getExpectedVerticalDistribution( scrollBar, SELECTION );
    int width = getExpectedWidth( scrollBar );
    assertThat( scrollBar )
      .hasUpBounds( CLEARANCE, 0, width, BUTTON_LENGTH )
      .hasDragBounds( 0, 0, 0, 0 )
      .hasDownBounds( CLEARANCE, distribution.downStart, width, BUTTON_LENGTH );
  }

  @Test
  public void layoutVerticalWithUndercutOfTwoTimesButtonLength() {
    shell.setSize( 475, BUTTON_LENGTH * 2 );
    FlatScrollBar scrollBar = createScrollBar( VERTICAL, SELECTION );

    shell.layout();

    int width = getExpectedWidth( scrollBar );
    int halfHeight = scrollBar.getControl().getSize().y / 2;
    assertThat( scrollBar )
      .hasUpBounds( CLEARANCE, 0, width, halfHeight )
      .hasDragBounds( 0, 0, 0, 0 )
      .hasDownBounds( CLEARANCE, halfHeight, width, halfHeight );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void layoutVerticalWithMaximumSelection() {
    FlatScrollBar scrollBar = createScrollBar( VERTICAL, DEFAULT_MAXIMUM );

    shell.layout();

    ComponentDistribution distribution = getExpectedVerticalDistribution( scrollBar, scrollBar.getSelection() );
    int width = getExpectedWidth( scrollBar );
    assertThat( scrollBar )
      .hasUpBounds( CLEARANCE, 0, width, BUTTON_LENGTH )
      .hasUpFastBounds( CLEARANCE, BUTTON_LENGTH, width, distribution.upFastLength )
      .hasDragBounds( CLEARANCE, distribution.dragStart, width, distribution.dragLength )
      .hasDownFastBounds( CLEARANCE, distribution.downFastStart, width, distribution.downFastLength )
      .hasDownBounds( CLEARANCE, distribution.downStart, width, BUTTON_LENGTH );
  }


  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void layoutVerticalWithMaximumSelectionAndDragLengthRounding() {
    shell.setSize( 500, 500 );
    FlatScrollBar scrollBar = createScrollBar( VERTICAL, DEFAULT_MAXIMUM );

    shell.layout();

    ComponentDistribution distribution = getExpectedVerticalDistribution( scrollBar, scrollBar.getSelection() );
    int width = getExpectedWidth( scrollBar );
    assertThat( scrollBar )
      .hasDragBounds( CLEARANCE, distribution.dragStart, width, distribution.dragLength + 1 )
      .hasDownFastBounds( CLEARANCE, distribution.downFastStart, width, distribution.downFastLength - 1 );
  }

  @Test
  public void setDefaultSizeVertical() {
    Point initialSize = new Point( BAR_BREADTH + 2, 5 );
    Control control = createControl( initialSize );

    VERTICAL.setDefaultSize( control );

    assertThat( control.getSize() )
      .isEqualTo( new Point( BAR_BREADTH, initialSize.y ) );
  }

  @Test
  public void computeSizeVertical() {
    int hHint = 10;

    Point actual = VERTICAL.computeSize( null, SWT.DEFAULT, hHint, true );

    assertThat( actual ).isEqualTo( new Point( BAR_BREADTH, hHint ) );
  }

  @Test
  public void computeSizeVerticalIfHHintIsDefault() {
    Rectangle clientArea = shell.getClientArea();
    Composite composite = new Composite( shell, SWT.NONE );

    Point actual = Orientation.VERTICAL.computeSize( composite, SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( new Point( BAR_BREADTH, clientArea.height ) );
  }

  @Test
  public void expandMaximumVertical() {
    createDrawingOrderPredecessorControl();
    Rectangle bounds = new Rectangle( 10, 4, 0, 40 );
    Control control = createControl( bounds );

    VERTICAL.expand( control );

    assertTopOfDrawingOrder( control );
    assertThat( control.getBounds() )
      .isEqualTo( new Rectangle( bounds.x - MAX_EXPAND, bounds.y, bounds.width + MAX_EXPAND, bounds.height ) );
  }

  @Test
  public void expandVerticalMinimum() {
    createDrawingOrderPredecessorControl();
    Rectangle bounds = new Rectangle( 10, 4, 10, 40 );
    Control control = createControl( bounds );

    VERTICAL.expand( control );

    assertTopOfDrawingOrder( control );
    assertThat( control.getBounds() )
      .isEqualTo( new Rectangle( bounds.x, bounds.y, bounds.width, bounds.height ) );
  }

  private FlatScrollBar createScrollBar( Orientation orientation, int selection ) {
    FlatScrollBar result = new FlatScrollBar( shell, orientation );
    result.setSelectionInternal( selection );
    return result;
  }

  private static ComponentDistribution getExpectedHorizontalDistribution( FlatScrollBar bar, int selection ) {
    int width = bar.getControl().getSize().x;
    return new ComponentDistribution( width, DEFAULT_MAXIMUM, selection, DEFAULT_THUMB );
  }

  private static int getExpectedHeight( FlatScrollBar scrollBar ) {
    return scrollBar.getControl().getSize().y - CLEARANCE - 1;
  }

  private static ComponentDistribution getExpectedVerticalDistribution( FlatScrollBar scrollBar, int selection ) {
    int height = scrollBar.getControl().getSize().y;
    return new ComponentDistribution( height, DEFAULT_MAXIMUM, selection, DEFAULT_THUMB );
  }

  private static int getExpectedWidth( FlatScrollBar scrollBar ) {
    return scrollBar.getControl().getSize().x - CLEARANCE - 1;
  }

  private Control createControl( Rectangle bounds ) {
    Control result = new Label( shell, SWT.NONE );
    result.setBounds( bounds );
    return result;
  }

  private Control createControl( Point size ) {
    Control result = new Label( shell, SWT.NONE );
    result.setSize( size );
    return result;
  }

  private void createDrawingOrderPredecessorControl() {
    createControl( new Point( 2, 3 ) );
  }

  private void assertTopOfDrawingOrder( Control control ) {
    assertThat( shell.getChildren() ).hasSize( 2 ).startsWith( control );
  }
}