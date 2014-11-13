package com.codeaffine.eclipse.swt.util;

import static com.codeaffine.eclipse.swt.util.ControlAssert.assertThat;
import static com.codeaffine.eclipse.swt.util.DragDetectorAssert.assertThat;
import static com.codeaffine.eclipse.swt.util.MouseEventHelper.createMouseEvent;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.junit.Before;
import org.junit.Test;

public class DragDetectorTest {

  private DragDetector dragDetector;
  private Control control;

  @Before
  public void setUp() {
    control = mock( Control.class );
    dragDetector = new DragDetector( control, 3 );
  }

  @Test
  public void dragDetectorDisablesDragDetection() {
    verify( control ).setDragDetect( false );
  }

  @Test
  public void mouseMove() {
    MouseEvent mouseEvent = createLeftButtonMouseEvent( control, 10, 10, SWT.BUTTON1 );

    dragDetector.mouseMove( mouseEvent );

    assertThat( control )
      .hasBeenNotifiedAboutEvent( SWT.DragDetect )
        .that()
          .hasX( mouseEvent.x )
          .hasY( mouseEvent.y )
          .hasType( SWT.DragDetect );
    assertThat( dragDetector )
      .dragEventGeneratedIsSet()
      .hasLastMouseX( mouseEvent.x )
      .hasLastMouseY( mouseEvent.y );
  }

  @Test
  public void mouseMoveIfMovementIsTooSmall() {
    MouseEvent mouseEvent = createLeftButtonMouseEvent( control, 1, 1, SWT.BUTTON1 );

    dragDetector.mouseMove( mouseEvent );

    assertThat( control )
      .hasNotBeenNotifiedAboutEvent( SWT.DragDetect );
    assertThat( dragDetector )
      .dragEventGeneratedFlagIsNotSet()
      .hasLastMouseX( mouseEvent.x )
      .hasLastMouseY( mouseEvent.y );
  }

  @Test
  public void mouseMoveIfButton1StateMaskIsNotSet() {
    MouseEvent mouseEvent = createLeftButtonMouseEvent( control, 10, 10, SWT.BUTTON2 );

    dragDetector.mouseMove( mouseEvent );

    assertThat( control )
      .hasNotBeenNotifiedAboutEvent( SWT.DragDetect );
    assertThat( dragDetector )
      .dragEventGeneratedFlagIsNotSet()
      .hasLastMouseX( 0 )
      .hasLastMouseY( 0 );
  }

  @Test
  public void mouseMoveIfLastDragEventIsNotHandledYet() {
    MouseEvent mouseEvent = createLeftButtonMouseEvent( control, 10, 10, SWT.BUTTON1 );
    dragDetector.dragEventGenerated = true;

    dragDetector.mouseMove( mouseEvent );

    assertThat( control )
      .hasNotBeenNotifiedAboutEvent( SWT.DragDetect );
    assertThat( dragDetector )
      .dragEventGeneratedIsSet()
      .hasLastMouseX( mouseEvent.x )
      .hasLastMouseY( mouseEvent.y );
  }

  @Test
  public void dragHandled() {
    dragDetector.dragEventGenerated = true;

    dragDetector.dragHandled();

    assertThat( control )
      .hasNotBeenNotifiedAboutEvent( SWT.DragDetect );
    assertThat( dragDetector )
      .dragEventGeneratedFlagIsNotSet();
  }

  static MouseEvent createLeftButtonMouseEvent( Control control, int x, int y, int stateMask ) {
    return createMouseEvent( control, x, y, stateMask, SWT.BUTTON1 );
  }
}