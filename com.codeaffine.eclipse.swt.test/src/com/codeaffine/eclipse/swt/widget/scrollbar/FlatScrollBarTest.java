/**
 * Copyright (c) 2014 - 2017 Frank Appel
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
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_INCREMENT;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_MAXIMUM;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_MINIMUM;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_PAGE_INCREMENT;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_SELECTION;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_THUMB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class FlatScrollBarTest {

  @Rule public final ConditionalIgnoreRule ignoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private FlatScrollBar scrollBar;
  private Shell shell;

  @Before
  public void setUp() {
    shell = displayHelper.createShell( SWT.SHELL_TRIM );
    shell.setBackgroundMode( SWT.INHERIT_DEFAULT );
    shell.setLayout( new FillLayout( SWT.HORIZONTAL ) );
    shell.setBounds( 250, 200, 500, 400 );
    shell.setBackground( Display.getCurrent().getSystemColor( SWT.COLOR_LIST_BACKGROUND ) );
    shell.open();
    scrollBar = new FlatScrollBar( shell, SWT.HORIZONTAL );
  }

  @Test( expected = UnsupportedOperationException.class )
  public void setLayout() {
    scrollBar.setLayout( null );
  }

  @Test
  public void defaultDirection() {
    FlatScrollBar flatScrollBar = new FlatScrollBar( shell, SWT.NONE );

    Direction actual = flatScrollBar.getDirection();

    assertThat( actual ).isSameAs( Direction.VERTICAL );
  }

  @Test
  public void getDirection() {
    Direction actual = scrollBar.getDirection();

    assertThat( actual ).isSameAs( Direction.HORIZONTAL );
  }

  @Test
  public void getStyle() {
    int actual = scrollBar.getStyle();

    assertThat( actual & SWT.HORIZONTAL ).isEqualTo( SWT.HORIZONTAL );
  }

  @Test
  public void getMinimum() {
    int actual = scrollBar.getMinimum();

    assertThat( actual ).isEqualTo( DEFAULT_MINIMUM );
  }

  @Test
  public void setMinimum() {
    int expected = DEFAULT_MAXIMUM - 1;
    scrollBar.setMinimum( expected );

    int actual = scrollBar.getMinimum();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setMinimumTwice() {
    scrollBar.setMinimum( DEFAULT_MAXIMUM - 1 );
    PaintListener listener = registerPaintListener( scrollBar );

    scrollBar.setMinimum( DEFAULT_MAXIMUM - 1 );

    assertThatNoLayoutUpdateHasBeenTriggered( listener );
  }

  @Test
  public void setMinimumWithTooLargeValue() {
    scrollBar.setMinimum( DEFAULT_MAXIMUM );

    int actual = scrollBar.getMinimum();

    assertThat( actual ).isEqualTo( DEFAULT_MINIMUM );
  }

  @Test
  public void setMinimumWithTooSmallValue() {
    scrollBar.setMinimum( -1 );

    int actual = scrollBar.getMinimum();

    assertThat( actual ).isEqualTo( DEFAULT_MINIMUM );
  }

  @Test
  public void setMinimumTriggersLayout() {
    shell.layout();

    Rectangle before = scrollBar.drag.getControl().getBounds();
    scrollBar.setMinimum( 20 );
    Rectangle after = scrollBar.drag.getControl().getBounds();

    assertThat( after ).isNotEqualTo( before );
  }

  @Test
  public void getMaximum() {
    int actual = scrollBar.getMaximum();

    assertThat( actual ).isEqualTo( DEFAULT_MAXIMUM );
  }

  @Test
  public void setMaximum() {
    int expected = 110;
    scrollBar.setMaximum( expected );

    int actual = scrollBar.getMaximum();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setMaximumTwice() {
    int maximum = 110;
    scrollBar.setMaximum( maximum );
    PaintListener listener = registerPaintListener( scrollBar );

    scrollBar.setMaximum( maximum );

    assertThatNoLayoutUpdateHasBeenTriggered( listener );
  }

  @Test
  public void setMaximumWithValueEqualsMinimum() {
    scrollBar.setMinimum( 10 );
    scrollBar.setMaximum( 10 );

    int actual = scrollBar.getMaximum();

    assertThat( actual ).isEqualTo( DEFAULT_MAXIMUM );
  }

  @Test
  public void setMaximumWithNegativeValue() {
    scrollBar.setMaximum( -1 );

    int actual = scrollBar.getMaximum();

    assertThat( actual ).isEqualTo( DEFAULT_MAXIMUM );
  }

  @Test
  public void setMaximumTriggersLayout() {
    shell.layout();

    Rectangle before = scrollBar.drag.getControl().getBounds();
    scrollBar.setMaximum( 80 );
    Rectangle after = scrollBar.drag.getControl().getBounds();

    assertThat( after ).isNotEqualTo( before );
  }

  @Test
  public void getThumb() {
    int actual = scrollBar.getThumb();

    assertThat( actual ).isEqualTo( DEFAULT_THUMB );
  }

  @Test
  public void getThumbOnMaximumAdjustment() {
    scrollBar.setMaximum( 1 );

    int actual = scrollBar.getThumb();

    assertThat( actual ).isEqualTo( 1 );
  }

  @Test
  public void getThumbOnMinimumAdjustment() {
    scrollBar.setMinimum( DEFAULT_MAXIMUM - 1 );

    int actual = scrollBar.getThumb();

    assertThat( actual ).isEqualTo( 1 );
  }

  @Test
  public void setThumb() {
    int expected = 20;
    scrollBar.setThumb( expected );

    int actual = scrollBar.getThumb();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setThumbTwice() {
    int thumb = 20;
    scrollBar.setThumb( thumb );
    PaintListener listener = registerPaintListener( scrollBar );

    scrollBar.setThumb( thumb );

    assertThatNoLayoutUpdateHasBeenTriggered( listener );
  }

  @Test
  public void setThumbWithTooSmallValue() {
    scrollBar.setThumb( 0 );

    int actual = scrollBar.getThumb();

    assertThat( actual ).isEqualTo( DEFAULT_THUMB );
  }

  @Test
  public void setThumbWithTooLargeValue() {
    scrollBar.setThumb( DEFAULT_MAXIMUM + 10 );

    int actual = scrollBar.getThumb();

    assertThat( actual ).isEqualTo( DEFAULT_MAXIMUM );
  }

  @Test
  public void setThumbTriggersLayout() {
    shell.layout();

    Rectangle before = scrollBar.drag.getControl().getBounds();
    scrollBar.setThumb( 20 );
    Rectangle after = scrollBar.drag.getControl().getBounds();

    assertThat( after ).isNotEqualTo( before );
  }

  @Test
  public void getIncrement() {
    int actual = scrollBar.getIncrement();

    assertThat( actual ).isEqualTo( DEFAULT_INCREMENT );
  }

  @Test
  public void setIncrement() {
    int expected = 10;
    scrollBar.setIncrement( expected );

    int actual = scrollBar.getIncrement();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setIncrementTwice() {
    int increment = 10;
    scrollBar.setIncrement( increment );
    PaintListener listener = registerPaintListener( scrollBar );

    scrollBar.setIncrement( increment );

    assertThatNoLayoutUpdateHasBeenTriggered( listener );
  }

  @Test
  public void getPageIncrement() {
    int actual = scrollBar.getPageIncrement();

    assertThat( actual ).isEqualTo( DEFAULT_PAGE_INCREMENT );
  }

  @Test
  public void setPageIncrement() {
    int expected = 25;
    scrollBar.setPageIncrement( expected );

    int actual = scrollBar.getPageIncrement();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void getSelection() {
    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( DEFAULT_SELECTION );
  }

  @Test
  public void getSelectionOnMinimumAdjustment() {
    scrollBar.setMinimum( DEFAULT_MAXIMUM - 1 );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( 99 );
  }

  @Test
  public void getSelectionOnMaximumAdjustment() {
    scrollBar.setThumb( 1 );
    scrollBar.setSelectionInternal( 10, SWT.NONE );
    scrollBar.setMaximum( 2 );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( 1 );
  }

  @Test
  public void getSelectionAfterThumbEnlargement() {
    scrollBar.setSelection( DEFAULT_MAXIMUM - DEFAULT_THUMB );
    scrollBar.setThumb( DEFAULT_THUMB * 2 );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( DEFAULT_MAXIMUM - DEFAULT_THUMB * 2 );
  }

  @Test
  public void setSelection() {
    int expected = 12;
    scrollBar.setSelection( expected );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setSelectionTwice() {
    int selection = 12;
    scrollBar.setSelection( selection );
    PaintListener listener = registerPaintListener( scrollBar );

    scrollBar.setSelection( selection );

    assertThatNoLayoutUpdateHasBeenTriggered( listener );
  }

  @Test
  public void setSelectionWithValueBelowMinimum() {
    scrollBar.setSelection( DEFAULT_MINIMUM - 1 );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( scrollBar.getMinimum() );
  }


  @Test
  public void setSelectionWithMaximumValue() {
    scrollBar.setSelection( DEFAULT_MAXIMUM );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( scrollBar.getMaximum() - scrollBar.getThumb() );
  }

  @Test
  public void setSelectionTriggersLayout() {
    shell.layout();

    Rectangle before = scrollBar.drag.getControl().getBounds();
    scrollBar.setSelection( 20 );
    Rectangle after = scrollBar.drag.getControl().getBounds();

    assertThat( after ).isNotEqualTo( before );
  }

  @Test
  public void setSelectionInternal() {
    int expected = 12;
    scrollBar.setSelectionInternal( expected, SWT.NONE );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void setSelectionInternalWithValueBelowMinimum() {
    scrollBar.setSelectionInternal( DEFAULT_MINIMUM - 1, SWT.NONE );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( scrollBar.getMinimum() );
  }

  @Test
  public void setSelectionInternalWithMaximumValue() {
    scrollBar.setSelectionInternal( DEFAULT_MAXIMUM, SWT.NONE );

    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( scrollBar.getMaximum() - scrollBar.getThumb() );
  }

  @Test
  public void setSelectionInternalTriggersLayout() {
    shell.layout();

    Rectangle before = scrollBar.drag.getControl().getBounds();
    scrollBar.setSelectionInternal( 20, SWT.NONE );
    Rectangle after = scrollBar.drag.getControl().getBounds();

    assertThat( after ).isNotEqualTo( before );
  }

  @Test
  public void resize() {
    shell.layout();

    Rectangle before = scrollBar.drag.getControl().getBounds();
    shell.setSize( 800, 800 );
    Rectangle after = scrollBar.drag.getControl().getBounds();

    assertThat( after ).isNotEqualTo( before );
  }

  @Test
  public void initialSizeSettings() {
    Point size = scrollBar.getSize();

    assertThat( size.y ).isEqualTo( FlatScrollBar.BAR_BREADTH );
  }

  @Test
  public void addSelectionListener() {
    ArgumentCaptor<SelectionEvent> captor = forClass( SelectionEvent.class );
    SelectionListener listener = mock( SelectionListener.class );

    scrollBar.addSelectionListener( listener );
    scrollBar.setSelectionInternal( 2, SWT.DRAG );

    verify( listener ).widgetSelected( captor.capture() );
    assertThat( captor.getValue().widget ).isSameAs( scrollBar );
    assertThat( captor.getValue().detail ).isEqualTo( SWT.DRAG );
    assertThat( scrollBar.getSelection() ).isEqualTo( 2 );
  }

  @Test
  public void removeSelectionListener() {
    SelectionListener listener = mock( SelectionListener.class );
    scrollBar.addSelectionListener( listener );

    scrollBar.removeSelectionListener( listener );
    scrollBar.setSelectionInternal( 2, SWT.NONE );

    verify( listener, never() ).widgetSelected( any( SelectionEvent.class ) );
  }

  @Test
  public void addUntypedSelectionListener() {
    ArgumentCaptor<Event> captor = forClass( Event.class );
    Listener listener = mock( Listener.class );

    scrollBar.addListener( SWT.Selection, listener );
    scrollBar.setSelectionInternal( 2, SWT.DRAG );

    verify( listener ).handleEvent( captor.capture() );
    assertThat( captor.getValue().widget ).isSameAs( scrollBar );
    assertThat( captor.getValue().detail ).isEqualTo( SWT.DRAG );
    assertThat( scrollBar.getSelection() ).isEqualTo( 2 );
  }

  @Test
  public void removeUntypedSelectionListener() {
    Listener listener = mock( Listener.class );
    scrollBar.addListener( SWT.Selection, listener );

    scrollBar.removeListener( SWT.Selection, listener );
    scrollBar.setSelectionInternal( 2, SWT.NONE );

    verify( listener, never() ).handleEvent( any( Event.class ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void addCommonListener() {
    ArgumentCaptor<Event> captor = forClass( Event.class );
    Listener listener = mock( Listener.class );

    scrollBar.addListener( SWT.FocusIn, listener );
    scrollBar.forceFocus();

    verify( listener ).handleEvent( captor.capture() );
    assertThat( captor.getValue().widget ).isSameAs( scrollBar );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void removeCommonListener() {
    Listener listener = mock( Listener.class );
    scrollBar.addListener( SWT.FocusIn, listener );

    scrollBar.removeListener( SWT.FocusIn, listener );
    scrollBar.forceFocus();

    verify( listener, never() ).handleEvent( any( Event.class ) );
  }

  @Test
  public void setSelectionDoesNotTriggerScrollEvent() {
    SelectionListener listener = mock( SelectionListener.class );

    scrollBar.addSelectionListener( listener );
    scrollBar.setSelection( 2 );

    verify( listener, never() ).widgetSelected( any( SelectionEvent.class ) );
  }

  @Test
  public void setSelectionOnDrag() {
    scrollBar.notifyListeners( SWT.DRAG );
    scrollBar.setSelection( 12 );
    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( DEFAULT_SELECTION );
  }

  @Test
  public void setSelectionOnDragWithUnrelatedType() {
    scrollBar.notifyListeners( SWT.DRAG );
    scrollBar.notifyListeners( SWT.HOME );
    scrollBar.setSelection( 12 );
    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( DEFAULT_SELECTION );
  }

  @Test
  public void setSelectionAfterDragEnd() {
    int expected = 12;

    scrollBar.notifyListeners( SWT.DRAG );
    scrollBar.notifyListeners( SWT.NONE );
    scrollBar.setSelection( expected );
    int actual = scrollBar.getSelection();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void notifyListeners() {
    ArgumentCaptor<SelectionEvent> captor = forClass( SelectionEvent.class );
    SelectionListener listener = mock( SelectionListener.class );
    scrollBar.addSelectionListener( listener );
    scrollBar.setSelection( 2 );

    scrollBar.notifyListeners( SWT.DRAG );

    verify( listener ).widgetSelected( captor.capture() );
    assertThat( captor.getValue().widget ).isSameAs( scrollBar );
    assertThat( captor.getValue().detail ).isEqualTo( SWT.DRAG );
    assertThat( scrollBar.getSelection() ).isEqualTo( 2 );
  }

  @Test
  public void layoutTriggersPaint() {
    PaintListener listener = registerPaintListener( scrollBar );

    Point size = scrollBar.getSize();
    scrollBar.setSize( size.x + 1, size.y );
    flushPendingEvents();

    verify( listener, atLeastOnce() ).paintControl( any( PaintEvent.class ) );
  }

  @Test
  public void controlLayout() {
    Layout layout = scrollBar.getLayout();

    assertThat( layout ).isExactlyInstanceOf( FlatScrollBarLayout.class );
  }


  private static PaintListener registerPaintListener( FlatScrollBar scrollBar ) {
    PaintListener result = mock( PaintListener.class );
    scrollBar.addPaintListener( result );
    return result;
  }

  private static void assertThatNoLayoutUpdateHasBeenTriggered( PaintListener listener ) {
    verify( listener, never() ).paintControl( any( PaintEvent.class ) );
  }
}