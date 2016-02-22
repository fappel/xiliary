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
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.equipScrollBarWithListener;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.verifyNotification;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Collection;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InOrder;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.MouseWheelSupport.ScrollBarAdapter;
import com.codeaffine.eclipse.swt.widget.scrollbar.MouseWheelSupport.SliderAdapter;

@RunWith( value = Parameterized.class )
public class MouseWheelSupportTest {

  @Parameters
  public static Collection<Object[]> data() {
    return DirectionHelper.valuesForParameterizedTests();
  }

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private final int direction;

  public MouseWheelSupportTest( int direction ) {
    this.direction = direction;
  }

  @Test
  public void sliderAdapterWidgetSelected() {
    MouseWheelSupport mouseWheelSupport = mock( MouseWheelSupport.class );
    SliderAdapter sliderAdapter = new SliderAdapter( mouseWheelSupport );

    sliderAdapter.widgetSelected( fakeEvent( direction ) );

    InOrder order = inOrder( mouseWheelSupport );
    order.verify( mouseWheelSupport ).updateScrollBarSelection( direction );
    order.verify( mouseWheelSupport ).copySettings();
    order.verifyNoMoreInteractions();
  }

  @Test
  public void scrollBarAdapterControlResized() {
    MouseWheelSupport mouseWheelSupport = mock( MouseWheelSupport.class );
    ScrollBarAdapter scrollBarAdapter = new ScrollBarAdapter( mouseWheelSupport );

    scrollBarAdapter.controlResized( null );

    verify( mouseWheelSupport ).copySettings();
    verifyNoMoreInteractions( mouseWheelSupport );
  }

  @Test
  public void scrollBarAdapterControlMoved() {
    MouseWheelSupport mouseWheelSupport = mock( MouseWheelSupport.class );
    ScrollBarAdapter scrollBarAdapter = new ScrollBarAdapter( mouseWheelSupport );

    scrollBarAdapter.controlMoved( null );

    verify( mouseWheelSupport ).copySettings();
    verifyNoMoreInteractions( mouseWheelSupport );
  }

  @Test
  public void scrollBarAdapterSelectionChanged() {
    MouseWheelSupport mouseWheelSupport = mock( MouseWheelSupport.class );
    ScrollBarAdapter scrollBarAdapter = new ScrollBarAdapter( mouseWheelSupport );

    scrollBarAdapter.widgetSelected( null );

    verify( mouseWheelSupport ).copySettings();
    verifyNoMoreInteractions( mouseWheelSupport );
  }

  @Test
  public void scrollBarAdapterDisposed() {
    MouseWheelSupport mouseWheelSupport = mock( MouseWheelSupport.class );
    ScrollBarAdapter scrollBarAdapter = new ScrollBarAdapter( mouseWheelSupport );

    scrollBarAdapter.widgetDisposed( null );

    verify( mouseWheelSupport ).dispose();
    verifyNoMoreInteractions( mouseWheelSupport );
  }

  @Test
  public void getControl() {
    FlatScrollBar scrollBar = createScrollBar();
    MouseWheelSupport mouseWheelSupport = new MouseWheelSupport( scrollBar );
    mouseWheelSupport.create();

    Control actual = mouseWheelSupport.getControl();

    assertThat( actual ).isInstanceOf( Slider.class );
  }

  @Test
  public void testDispose() {
    FlatScrollBar scrollBar = createScrollBar();
    MouseWheelSupport mouseWheelSupport = new MouseWheelSupport( scrollBar );
    mouseWheelSupport.create();

    mouseWheelSupport.dispose();

    try {
      scrollBar.setLocation( new Point( 50, 60 ) );
    } catch( RuntimeException shouldNotHappen ) {
      fail();
    }
    assertThat( mouseWheelSupport.getControl().isDisposed() ).isTrue();
  }

  @Test
  public void disposeOfScrollBarControl() {
    FlatScrollBar scrollBar = createScrollBar();
    MouseWheelSupport mouseWheelSupport = new MouseWheelSupport( scrollBar );
    mouseWheelSupport.create();

    scrollBar.dispose();

    assertThat( mouseWheelSupport.getControl().isDisposed() ).isTrue();
  }

  @Test
  public void disposeAfterScrollBarControlHasBeenDisposed() {
    FlatScrollBar scrollBar = createScrollBar();
    MouseWheelSupport mouseWheelSupport = new MouseWheelSupport( scrollBar );
    mouseWheelSupport.create();

    scrollBar.dispose();
    mouseWheelSupport.dispose();

    assertThat( mouseWheelSupport.getControl().isDisposed() ).isTrue();
    assertThat( scrollBar.isDisposed() ).isTrue();
  }

  @Test
  public void create() {
    FlatScrollBar scrollBar = createScrollBar();
    MouseWheelSupport mouseWheelSupport = new MouseWheelSupport( scrollBar );

    mouseWheelSupport.create();
    assertThat( mouseWheelSupport.getControl() ).isNotNull();
  }

  @Test
  public void createRegistersResizeObserver() {
    FlatScrollBar scrollBar = createScrollBar();
    scrollBar.setBounds( new Rectangle( 10, 20, 30, 40 ) );
    MouseWheelSupport mouseWheelSupport = new MouseWheelSupport( scrollBar );
    mouseWheelSupport.create();

    Point expected = new Point( 50, 60 );
    scrollBar.setSize( expected );
    Point actual = mouseWheelSupport.getControl().getSize();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void createRegistersMoveObserver() {
    FlatScrollBar scrollBar = createScrollBar();
    scrollBar.setBounds( new Rectangle( 10, 20, 30, 40 ) );
    MouseWheelSupport mouseWheelSupport = new MouseWheelSupport( scrollBar );
    mouseWheelSupport.create();

    Point expected = new Point( 50, 60 );
    scrollBar.setLocation( expected );
    Point actual = mouseWheelSupport.getControl().getLocation();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void createCopiesSettings() {
    FlatScrollBar scrollBar = createScrollBar();
    configureScrollBar( scrollBar, new Rectangle( 10, 20, 30, 40 ), 309, 10, 4, 20, 40, 68 );
    MouseWheelSupport mouseWheelSupport = new MouseWheelSupport( scrollBar );

    mouseWheelSupport.create();

    verifySliderSettings( mouseWheelSupport, new Rectangle( 10, 20, 30, 40 ), 309, 10, 4, 20, 40, 68 );
  }

  @Test
  public void copySettings() {
    FlatScrollBar scrollBar = createScrollBar();
    MouseWheelSupport mouseWheelSupport = new MouseWheelSupport( scrollBar );
    mouseWheelSupport.create();
    configureScrollBar( scrollBar, new Rectangle( 10, 20, 30, 40 ), 309, 10, 4, 20, 40, 68 );

    mouseWheelSupport.copySettings();

    verifySliderSettings( mouseWheelSupport, new Rectangle( 10, 20, 30, 40 ), 309, 10, 4, 20, 40, 68 );
  }

  @Test
  public void copySettingsIfSliderHasLayoutData() {
    FlatScrollBar scrollBar = createScrollBar();
    Rectangle expected = new Rectangle( 10, 20, 30, 40 );
    scrollBar.setBounds( expected );
    MouseWheelSupport mouseWheelSupport = new MouseWheelSupport( scrollBar );
    mouseWheelSupport.create();
    Slider slider = ( Slider )mouseWheelSupport.getControl();
    slider.setLayoutData( new FormData() );
    scrollBar.setBounds( new Rectangle( 50, 60, 70, 80 ) );

    mouseWheelSupport.copySettings();

    assertThat( slider.getBounds() ).isEqualTo( expected  );
  }

  @Test
  public void updateScrollBarSelection() {
    FlatScrollBar scrollBar = createScrollBar();
    SelectionListener listener = equipScrollBarWithListener( scrollBar );
    MouseWheelSupport mouseWheelSupport = new MouseWheelSupport( scrollBar );
    mouseWheelSupport.create();
    Slider slider = ( Slider )mouseWheelSupport.getControl();
    slider.setSelection( 10 );

    mouseWheelSupport.updateScrollBarSelection( direction );

    SelectionEvent event = verifyNotification( listener );
    assertThat( event.widget ).isSameAs( scrollBar );
    assertThat( event.detail ).isEqualTo( direction );
    assertThat( scrollBar.getSelection() ).isEqualTo( 10 );
  }

  private FlatScrollBar createScrollBar() {
    Shell parent = displayHelper.createShell();
    FlatScrollBar result = new FlatScrollBar( parent, direction );
    flushPendingEvents();
    return result;
  }

  private static void configureScrollBar(
    FlatScrollBar bar, Rectangle bounds, int max, int min, int increment, int pageIncrement, int thumb, int selection )
  {
    bar.setBounds( bounds );
    bar.setMaximum( max );
    bar.setMinimum( min );
    bar.setIncrement( increment );
    bar.setPageIncrement( pageIncrement );
    bar.setThumb( thumb );
    bar.setSelection( selection );
  }

  private static void verifySliderSettings(
    MouseWheelSupport support, Rectangle bounds, int max, int min, int inc, int pageInc, int thumb, int selection  )
  {
    Slider slider = ( Slider )support.getControl();
    assertThat( slider.getMaximum() ).isEqualTo( max );
    assertThat( slider.getMinimum() ).isEqualTo( min );
    assertThat( slider.getIncrement() ).isEqualTo( inc );
    assertThat( slider.getPageIncrement() ).isEqualTo( pageInc );
    assertThat( slider.getThumb() ).isEqualTo( thumb );
    assertThat( slider.getSelection() ).isEqualTo( selection );
    assertThat( slider.getBounds() ).isEqualTo( bounds );
  }

  private static SelectionEvent fakeEvent( int direction ) {
    Event event = new Event();
    event.widget = mock( FlatScrollBar.class );
    event.detail = direction;
    return new SelectionEvent( event );
  }
}