package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_MAXIMUM;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_THUMB;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.equipScrollBarWithListener;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.verifyNotification;
import static java.lang.Math.max;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

@RunWith( value = Parameterized.class )
public class MouseWheelShifterTest {

  @Parameters
  public static Collection<Object[]> data() {
    return DirectionHelper.valuesForParameterizedTests();
  }

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private final int direction;

  private FlatScrollBar scrollBar;
  private Shell parent;

  public MouseWheelShifterTest( int direction ) {
    this.direction = direction;
  }

  @Before
  public void setUp() {
    parent = createShell( displayHelper );
    scrollBar = new FlatScrollBar( parent, direction );
    parent.open();
    parent.layout();
  }

  @Test
  public void handleEvent() {
    SelectionListener listener = equipScrollBarWithListener( scrollBar );
    Point dragLocation = scrollBar.drag.getControl().getLocation();

    trigger( getSWTMouseWheelEventType() ).withCount( -3 ).on( parent );

    SelectionEvent event = verifyNotification( listener );
    assertThat( event.widget ).isSameAs( scrollBar );
    assertThat( scrollBar.getSelection() ).isEqualTo( 2 );
    assertThat( scrollBar.drag.getControl().getLocation() ).isNotEqualTo( dragLocation );
  }

  @Test
  public void handleEventWithScrollToMaximum() {
    SelectionListener listener = equipScrollBarWithListener( scrollBar );
    Point size = scrollBar.getSize();

    trigger( getSWTMouseWheelEventType() ).withCount( -max( size.x, size.y ) ).on( parent );

    SelectionEvent event = verifyNotification( listener );
    assertThat( event.widget ).isSameAs( scrollBar );
    assertThat( scrollBar.getSelection() ).isEqualTo( DEFAULT_MAXIMUM - DEFAULT_THUMB );
  }

  @Test
  public void handleEventWithBackwardScroll() {
    Point size = scrollBar.getSize();
    trigger( getSWTMouseWheelEventType() ).withCount( -max( size.x, size.y ) ).on( parent );

    trigger( getSWTMouseWheelEventType() ).withCount( max( size.x, size.y ) ).on( parent );

    assertThat( scrollBar.getSelection() ).isZero();
  }

  @Test
  public void handleEventIfDragSizeUsesCompleteSlideRange() {
    scrollBar.setThumb( scrollBar.getMaximum() );
    SelectionListener listener = equipScrollBarWithListener( scrollBar );

    trigger( getSWTMouseWheelEventType() ).withCount( -3 ).on( parent );

    verify( listener, never() ).widgetSelected( any( SelectionEvent.class ) );
  }

  @Test
  public void dispose() {
    SelectionListener listener = equipScrollBarWithListener( scrollBar );
    scrollBar.dispose();

    trigger( getSWTMouseWheelEventType() ).withCount( -3 ).on( parent );

    verify( listener, never() ).widgetSelected( any( SelectionEvent.class ) );
  }

  private int getSWTMouseWheelEventType() {
    return direction == SWT.HORIZONTAL ? SWT.MouseHorizontalWheel : SWT.MouseVerticalWheel;
  }
}