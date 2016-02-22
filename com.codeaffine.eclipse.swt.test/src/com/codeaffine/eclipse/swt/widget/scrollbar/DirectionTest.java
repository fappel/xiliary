/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.codeaffine.eclipse.swt.widget.scrollbar.Direction.CLEARANCE;
import static com.codeaffine.eclipse.swt.widget.scrollbar.Direction.HORIZONTAL;
import static com.codeaffine.eclipse.swt.widget.scrollbar.Direction.VERTICAL;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_MAXIMUM;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_THUMB;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarAssert.assertThat;
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

public class DirectionTest {

  private static final int SELECTION = 12;
  private static final int BUTTON_LENGTH = 17;
  private static final int MAX_EXPAND = Direction.CLEARANCE;

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private Composite parent;

  @Before
  public void setUp() {
    Shell shell = displayHelper.createShell( SWT.RESIZE );
    shell.setBounds( 100, 100, 800, 800 );
    shell.setBackground( displayHelper.getDisplay().getSystemColor( SWT.COLOR_WHITE ) );
    parent = new Composite( shell, SWT.NONE );
    parent.setBounds( 100, 100, 480, 480 );
    parent.setLayout( new FillLayout() );
    shell.open();
  }

  @Test
  public void layoutHorizontal() {
    FlatScrollBar scrollBar = createScrollBar( SWT.HORIZONTAL, SELECTION );

    layoutParent();

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
    parent.setSize( BUTTON_LENGTH * 3, 475 );
    FlatScrollBar scrollBar = createScrollBar( SWT.HORIZONTAL, SELECTION );

    layoutParent();

    ComponentDistribution distribution = getExpectedHorizontalDistribution( scrollBar, SELECTION );
    int height = getExpectedHeight( scrollBar );
    assertThat( scrollBar )
      .hasUpBounds( 0, CLEARANCE, BUTTON_LENGTH, height )
      .hasDragBounds( 0, 0, 0, 0 )
      .hasDownBounds( distribution.downStart, CLEARANCE, BUTTON_LENGTH, height );
  }

  @Test
  public void layoutHorizontalWithUndercutOfTwoTimesButtonLength() {
    parent.setSize( BUTTON_LENGTH * 2, 475 );
    FlatScrollBar scrollBar = createScrollBar( SWT.HORIZONTAL, SELECTION );

    layoutParent();

    int height = getExpectedHeight( scrollBar );
    int halfWidth = scrollBar.getSize().x / 2;
    assertThat( scrollBar )
      .hasUpBounds( 0, CLEARANCE, halfWidth, height )
      .hasDragBounds( 0, 0, 0, 0 )
      .hasDownBounds( halfWidth, CLEARANCE, halfWidth, height );
  }

  @Test
  public void layoutHorizontalWithMaximumSelection() {
    FlatScrollBar scrollBar = createScrollBar( SWT.HORIZONTAL, DEFAULT_MAXIMUM );

    layoutParent();

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
    parent.setSize( 505, 505 );
    FlatScrollBar scrollBar = createScrollBar( SWT.HORIZONTAL, DEFAULT_MAXIMUM );

    layoutParent();

    ComponentDistribution distribution = getExpectedHorizontalDistribution( scrollBar, scrollBar.getSelection() );
    int height = getExpectedHeight( scrollBar );
    assertThat( scrollBar )
      .hasDragBounds( distribution.dragStart, CLEARANCE, distribution.dragLength + 1, height )
      .hasDownFastBounds( distribution.downFastStart, CLEARANCE, distribution.downFastLength - 1, height );
  }

  @Test
  public void setDefaultSizeHorizontal() {
    Point initialSize = new Point( 5, FlatScrollBar.BAR_BREADTH + 2 );
    Control control = createControl( initialSize );

    HORIZONTAL.setDefaultSize( control );

    assertThat( control.getSize() )
      .isEqualTo( new Point( initialSize.x, FlatScrollBar.BAR_BREADTH ) );
  }

  @Test
  public void computeSizeHorizontal() {
    int wHint = 10;

    Point actual = HORIZONTAL.computeSize( null, wHint, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( new Point( wHint, FlatScrollBar.BAR_BREADTH ) );
  }

  @Test
  public void computeSizeHorizontalIfWHintIsDefault() {
    Rectangle clientArea = parent.getClientArea();
    Composite composite = new Composite( parent, SWT.NONE );

    Point actual = HORIZONTAL.computeSize( composite, SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( new Point( clientArea.width, FlatScrollBar.BAR_BREADTH ) );
  }

  @Test
  public void expandMaximumHorizontal() {
    createDrawingOrderPredecessorControl();
    Rectangle bounds = new Rectangle( 4, 10, 40, 0 );
    Control control = createControl( bounds );

    HORIZONTAL.expand( control, MAX_EXPAND );
    flushPendingEvents();

    assertTopOfDrawingOrder( control );
    assertThat( control.getBounds() )
      .isEqualTo( new Rectangle( bounds.x, bounds.y - MAX_EXPAND, bounds.width, bounds.height + MAX_EXPAND ) );
  }

  @Test
  public void expandHorizontalMinimum() {
    createDrawingOrderPredecessorControl();
    Rectangle bounds = new Rectangle( 4, 10, 40, 10 );
    Control control = createControl( bounds );

    HORIZONTAL.expand( control, MAX_EXPAND );

    assertTopOfDrawingOrder( control );
    assertThat( control.getBounds() )
      .isEqualTo( new Rectangle( bounds.x, bounds.y, bounds.width, bounds.height ) );
  }

  @Test
  public void horizontalValue() {
    int actual = HORIZONTAL.value();

    assertThat( actual ).isEqualTo( SWT.HORIZONTAL );
  }

  @Test
  public void layoutVertical() {
    FlatScrollBar scrollBar = createScrollBar( SWT.VERTICAL, SELECTION );

    layoutParent();

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
    parent.setSize( 475, BUTTON_LENGTH * 3 );
    FlatScrollBar scrollBar = createScrollBar( SWT.VERTICAL, SELECTION );

    layoutParent();

    ComponentDistribution distribution = getExpectedVerticalDistribution( scrollBar, SELECTION );
    int width = getExpectedWidth( scrollBar );
    assertThat( scrollBar )
      .hasUpBounds( CLEARANCE, 0, width, BUTTON_LENGTH )
      .hasDragBounds( 0, 0, 0, 0 )
      .hasDownBounds( CLEARANCE, distribution.downStart, width, BUTTON_LENGTH );
  }

  @Test
  public void layoutVerticalWithUndercutOfTwoTimesButtonLength() {
    parent.setSize( 475, BUTTON_LENGTH * 2 );
    FlatScrollBar scrollBar = createScrollBar( SWT.VERTICAL, SELECTION );

    layoutParent();

    int width = getExpectedWidth( scrollBar );
    int halfHeight = scrollBar.getSize().y / 2;
    assertThat( scrollBar )
      .hasUpBounds( CLEARANCE, 0, width, halfHeight )
      .hasDragBounds( 0, 0, 0, 0 )
      .hasDownBounds( CLEARANCE, halfHeight, width, halfHeight );
  }

  @Test
  public void layoutVerticalWithMaximumSelection() {
    FlatScrollBar scrollBar = createScrollBar( SWT.VERTICAL, DEFAULT_MAXIMUM );

    layoutParent();

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
  public void layoutVerticalWithMaximumSelectionAndDragLengthRounding() {
    parent.setSize( 505, 505 );
    FlatScrollBar scrollBar = createScrollBar( SWT.VERTICAL, DEFAULT_MAXIMUM );

    layoutParent();

    ComponentDistribution distribution = getExpectedVerticalDistribution( scrollBar, scrollBar.getSelection() );
    int width = getExpectedWidth( scrollBar );
    assertThat( scrollBar )
      .hasDragBounds( CLEARANCE, distribution.dragStart, width, distribution.dragLength + 1 )
      .hasDownFastBounds( CLEARANCE, distribution.downFastStart, width, distribution.downFastLength - 1 );
  }

  @Test
  public void setDefaultSizeVertical() {
    Point initialSize = new Point( FlatScrollBar.BAR_BREADTH + 2, 5 );
    Control control = createControl( initialSize );

    VERTICAL.setDefaultSize( control );

    assertThat( control.getSize() )
      .isEqualTo( new Point( FlatScrollBar.BAR_BREADTH, initialSize.y ) );
  }

  @Test
  public void computeSizeVertical() {
    int hHint = 10;

    Point actual = VERTICAL.computeSize( null, SWT.DEFAULT, hHint, true );

    assertThat( actual ).isEqualTo( new Point( FlatScrollBar.BAR_BREADTH, hHint ) );
  }

  @Test
  public void computeSizeVerticalIfHHintIsDefault() {
    Rectangle clientArea = parent.getClientArea();
    Composite composite = new Composite( parent, SWT.NONE );

    Point actual = Direction.VERTICAL.computeSize( composite, SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( new Point( FlatScrollBar.BAR_BREADTH, clientArea.height ) );
  }

  @Test
  public void expandMaximumVertical() {
    createDrawingOrderPredecessorControl();
    Rectangle bounds = new Rectangle( 10, 4, 0, 40 );
    Control control = createControl( bounds );

    VERTICAL.expand( control, MAX_EXPAND );
    flushPendingEvents();

    assertTopOfDrawingOrder( control );
    assertThat( control.getBounds() )
      .isEqualTo( new Rectangle( bounds.x - MAX_EXPAND, bounds.y, bounds.width + MAX_EXPAND, bounds.height ) );
  }

  @Test
  public void expandVerticalMinimum() {
    createDrawingOrderPredecessorControl();
    Rectangle bounds = new Rectangle( 10, 4, 10, 40 );
    Control control = createControl( bounds );

    VERTICAL.expand( control, MAX_EXPAND );

    assertTopOfDrawingOrder( control );
    assertThat( control.getBounds() )
      .isEqualTo( new Rectangle( bounds.x, bounds.y, bounds.width, bounds.height ) );
  }

  @Test
  public void verticalValue() {
    int actual = VERTICAL.value();

    assertThat( actual ).isEqualTo( SWT.VERTICAL );
  }

  private void layoutParent() {
    parent.layout();
    flushPendingEvents();
  }

  private FlatScrollBar createScrollBar( int direction, int selection ) {
    FlatScrollBar result = new FlatScrollBar( parent, direction, BUTTON_LENGTH, MAX_EXPAND );
    result.setSelectionInternal( selection, SWT.ARROW_DOWN );
    return result;
  }

  private static ComponentDistribution getExpectedHorizontalDistribution( FlatScrollBar bar, int selection ) {
    int width = bar.getSize().x;
    return new ComponentDistribution( BUTTON_LENGTH, width, DEFAULT_MAXIMUM, selection, DEFAULT_THUMB );
  }

  private static int getExpectedHeight( FlatScrollBar scrollBar ) {
    return scrollBar.getSize().y - CLEARANCE - 1;
  }

  private static ComponentDistribution getExpectedVerticalDistribution( FlatScrollBar scrollBar, int selection ) {
    int height = scrollBar.getSize().y;
    return new ComponentDistribution( BUTTON_LENGTH, height, DEFAULT_MAXIMUM, selection, DEFAULT_THUMB );
  }

  private static int getExpectedWidth( FlatScrollBar scrollBar ) {
    return scrollBar.getSize().x - CLEARANCE - 1;
  }

  private Control createControl( Rectangle bounds ) {
    Control result = new Label( parent, SWT.NONE );
    result.setBounds( bounds );
    return result;
  }

  private Control createControl( Point size ) {
    Control result = new Label( parent, SWT.NONE );
    result.setSize( size );
    return result;
  }

  private void createDrawingOrderPredecessorControl() {
    createControl( new Point( 2, 3 ) );
  }

  private void assertTopOfDrawingOrder( Control control ) {
    assertThat( parent.getChildren() ).hasSize( 2 ).endsWith( control );
  }
}