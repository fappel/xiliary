package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrown;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.test.util.lang.ThrowableCaptor.Actor;

public class MouseTrackerTest {

  private static final Rectangle BOUNDS = new Rectangle( 10, 20, 30, Direction.BAR_BREADTH );
  private static final Rectangle BOUNDS_ON_MOUSE_OVER = new Rectangle( 10, 16, 30, 10 );

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private MouseTracker mouseTracker;
  private FlatScrollBar scrollBar;

  @Before
  public void setUp() {
    Shell shell = displayHelper.createShell();
    scrollBar = new FlatScrollBar( shell, SWT.HORIZONTAL );
    scrollBar.setBounds( BOUNDS );
    mouseTracker = new MouseTracker( scrollBar );
  }

  @Test
  public void mouseEnter() {
    mouseTracker.mouseEnter( null );

    Rectangle actual = scrollBar.getBounds();

    assertThat( actual ).isEqualTo( BOUNDS_ON_MOUSE_OVER );
  }

  @Test
  public void mouseEnterTwice() {
    mouseTracker.mouseEnter( null );
    mouseTracker.mouseExit( null );
    mouseTracker.mouseEnter( null );

    Rectangle actual = scrollBar.getBounds();

    assertThat( actual ).isEqualTo( BOUNDS_ON_MOUSE_OVER );
  }

  @Test
  public void mouseExit() throws InterruptedException {
    mouseTracker.mouseEnter( null );

    mouseTracker.mouseExit( null );
    Thread.sleep( MouseTracker.DELAY + 100 );
    flushPendingEvents();
    Rectangle actual = scrollBar.getBounds();

    assertThat( actual ).isEqualTo( BOUNDS );
  }

  @Test
  public void mouseExitAfterBoundsHasBeenChangedExternally() throws InterruptedException {
    Rectangle expected = new Rectangle( 10, 20, 30, 40 );
    mouseTracker.mouseEnter( null );
    scrollBar.setBounds( expected );

    mouseTracker.mouseExit( null );
    Thread.sleep( MouseTracker.DELAY + 100 );
    flushPendingEvents();
    Rectangle actual = scrollBar.getBounds();

    assertThat( actual ).isEqualTo( expected );
  }

  @Test
  public void runAfterEnterAndExit() {
    mouseTracker.mouseEnter( null );
    mouseTracker.mouseExit( null );

    mouseTracker.run();
    Rectangle actual = scrollBar.getBounds();

    assertThat( actual ).isEqualTo( BOUNDS );
  }

  @Test
  public void runAfterMouseEnter() {
    mouseTracker.mouseEnter( null );

    mouseTracker.run();
    Rectangle actual = scrollBar.getBounds();

    assertThat( actual ).isEqualTo( BOUNDS_ON_MOUSE_OVER );
  }

  @Test
  public void runAfterDisposeOfControl() {
    mouseTracker.mouseEnter( null );
    mouseTracker.mouseExit( null );
    scrollBar.dispose();

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        mouseTracker.run();
      }
    } );

    assertThat( actual ).isNull();
  }
}