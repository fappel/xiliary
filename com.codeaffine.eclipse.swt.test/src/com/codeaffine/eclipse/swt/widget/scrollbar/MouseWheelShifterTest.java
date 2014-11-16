package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_MAXIMUM;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_THUMB;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.equipScrollBarWithListener;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.verifyNotification;
import static com.codeaffine.eclipse.swt.widget.scrollbar.Orientation.HORIZONTAL;
import static java.lang.Math.max;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.eclipse.swt.SWT;
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
    return OrientationHelper.valuesForParameterizedTests();
  }

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private final Orientation orientation;

  private FlatScrollBar scrollBar;
  private Shell parent;

  public MouseWheelShifterTest( Orientation orientation ) {
    this.orientation = orientation;
  }

  @Before
  public void setUp() {
    parent = createShell( displayHelper );
    scrollBar = new FlatScrollBar( parent, orientation );
    parent.open();
    parent.layout();
  }

  @Test
  public void handleEvent() {
    ScrollListener listener = equipScrollBarWithListener( scrollBar );
    Point dragLocation = scrollBar.drag.getControl().getLocation();

    trigger( getSWTMouseWheelEventType() ).withCount( -3 ).on( parent );

    ScrollEvent event = verifyNotification( listener );
    assertThat( event.getSelection() ).isEqualTo( 2 );
    assertThat( scrollBar.drag.getControl().getLocation() ).isNotEqualTo( dragLocation );
  }

  @Test
  public void handleEventWithScrollToMaximum() {
    ScrollListener listener = equipScrollBarWithListener( scrollBar );
    Point size = scrollBar.getControl().getSize();

    trigger( getSWTMouseWheelEventType() ).withCount( -max( size.x, size.y ) ).on( parent );

    ScrollEvent event = verifyNotification( listener );
    assertThat( event.getSelection() ).isEqualTo( DEFAULT_MAXIMUM - DEFAULT_THUMB );
  }

  @Test
  public void handleEventWithBackwardScroll() {
    Point size = scrollBar.getControl().getSize();
    trigger( getSWTMouseWheelEventType() ).withCount( -max( size.x, size.y ) ).on( parent );

    trigger( getSWTMouseWheelEventType() ).withCount( max( size.x, size.y ) ).on( parent );

    assertThat( scrollBar.getSelection() ).isZero();
  }

  @Test
  public void handleEventIfDragSizeUsesCompleteSlideRange() {
    scrollBar.setThumb( scrollBar.getMaximum() );
    ScrollListener listener = equipScrollBarWithListener( scrollBar );

    trigger( getSWTMouseWheelEventType() ).withCount( -3 ).on( parent );

    verify( listener, never() ).selectionChanged( any( ScrollEvent.class ) );
  }

  @Test
  public void dispose() {
    ScrollListener listener = equipScrollBarWithListener( scrollBar );
    scrollBar.getControl().dispose();

    trigger( getSWTMouseWheelEventType() ).withCount( -3 ).on( parent );

    verify( listener, never() ).selectionChanged( any( ScrollEvent.class ) );
  }

  private int getSWTMouseWheelEventType() {
    return orientation == HORIZONTAL ? SWT.MouseHorizontalWheel : SWT.MouseVerticalWheel;
  }
}