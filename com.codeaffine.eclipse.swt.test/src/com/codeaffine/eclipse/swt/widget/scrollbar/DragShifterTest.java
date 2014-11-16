package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_MAXIMUM;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.DEFAULT_THUMB;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.equipScrollBarWithListener;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.verifyNotification;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

@RunWith( value = Parameterized.class )
public class DragShifterTest {

  @Parameters
  public static Collection<Object[]> data() {
    return OrientationHelper.valuesForParameterizedTests();
  }

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private final Orientation orientation;

  private FlatScrollBar scrollBar;

  public DragShifterTest( Orientation orientation ) {
    this.orientation = orientation;
  }

  @Before
  public void setUp() {
    Shell shell = createShell( displayHelper );
    scrollBar = new FlatScrollBar( shell, orientation );
    shell.open();
    shell.layout();
  }

  @Test
  public void run() {
    ScrollListener listener = equipScrollBarWithListener( scrollBar );
    Point dragLocation = getControl().getLocation();

    trigger( SWT.MouseDown ).at( 1, 1 ).on( getControl() );
    trigger( SWT.MouseMove ).at( 10, 10 ).withStateMask( SWT.BUTTON1 ).on( getControl() );

    ScrollEvent event = verifyNotification( listener );
    assertThat( event.getSelection() ).isEqualTo( 6 );
    assertThat( getControl().getLocation() ).isNotEqualTo( dragLocation );
  }

  @Test
  public void runWithDragToMaximum() {
    ScrollListener listener = equipScrollBarWithListener( scrollBar );
    Point size = scrollBar.getControl().getSize();

    trigger( SWT.MouseDown ).at( 0, 0 ).on( getControl() );
    trigger( SWT.MouseMove ).at( size.x, size.y ).withStateMask( SWT.BUTTON1 ).on( getControl() );

    ScrollEvent event = verifyNotification( listener );
    assertThat( event.getSelection() ).isEqualTo( DEFAULT_MAXIMUM - DEFAULT_THUMB );
  }

  @Test
  public void runWithBackwardDrag() {
    Point size = scrollBar.getControl().getSize();
    trigger( SWT.MouseDown ).at( 0, 0 ).on( getControl() );
    trigger( SWT.MouseMove ).at( size.x, size.y ).withStateMask( SWT.BUTTON1 ).on( getControl() );
    trigger( SWT.MouseUp ).at( size.x, size.y ).withStateMask( SWT.BUTTON1 ).on( getControl() );

    trigger( SWT.MouseDown ).at( 0, 0 ).on( getControl() );
    trigger( SWT.MouseMove ).at( -size.x, -size.y ).withStateMask( SWT.BUTTON1 ).on( getControl() );

    assertThat( scrollBar.getSelection() ).isZero();
  }

  @Test
  public void runIfDragSizeUsesCompleteSlideRange() {
    scrollBar.setThumb( scrollBar.getMaximum() );
    ScrollListener listener = equipScrollBarWithListener( scrollBar );

    trigger( SWT.MouseDown ).at( 1, 1 ).on( getControl() );
    trigger( SWT.MouseMove ).at( 10, 10 ).withStateMask( SWT.BUTTON1 ).on( getControl() );

    verify( listener, never() ).selectionChanged( any( ScrollEvent.class ) );
  }

  private Control getControl() {
    return scrollBar.drag.getControl();
  }
}