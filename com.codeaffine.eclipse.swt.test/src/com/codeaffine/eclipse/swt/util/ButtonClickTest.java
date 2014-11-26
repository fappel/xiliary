package com.codeaffine.eclipse.swt.util;

import static com.codeaffine.eclipse.swt.util.ButtonClick.LEFT_BUTTON;
import static com.codeaffine.eclipse.swt.util.MouseEventHelper.createMouseEvent;
import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith( JUnitParamsRunner.class )
public class ButtonClickTest {

  private static final int X_RANGE = 10;
  private static final int IN_X_RANGE = X_RANGE / 2;
  private static final int Y_RANGE = 20;
  private static final int IN_Y_RANGE = Y_RANGE / 2;

  private ButtonClick mouseClick;
  private Runnable action;

  @Before
  public void setUp() {
    mouseClick = new ButtonClick();
    action = mock( Runnable.class );
  }

  @Test
  public void arm() {
    MouseEvent event = createLeftButtonMouseEventOnControlStub( X_RANGE, Y_RANGE, IN_X_RANGE, IN_Y_RANGE );

    mouseClick.arm( event );
    boolean actual = mouseClick.isArmed();

    assertThat( actual ).isTrue();
  }

  @Test
  public void armWithNonLeftButton() {
    MouseEvent event = createRightButtonMouseEventOnControlStub( X_RANGE, Y_RANGE, IN_X_RANGE, IN_Y_RANGE );

    mouseClick.arm( event );
    boolean actual = mouseClick.isArmed();

    assertThat( actual ).isFalse();
  }

  @Test
  public void disarm() {
    MouseEvent event = createLeftButtonMouseEventOnControlStub( X_RANGE, Y_RANGE, IN_X_RANGE, IN_Y_RANGE );
    mouseClick.arm( event );

    mouseClick.disarm();
    boolean actual = mouseClick.isArmed();

    assertThat( actual ).isFalse();
  }

  @Test
  public void isArmed() {
    boolean actual = mouseClick.isArmed();

    assertThat( actual ).isFalse();
  }

  @Test
  public void trigger() {
    MouseEvent event = createLeftButtonMouseEventOnControlStub( X_RANGE, Y_RANGE, IN_X_RANGE, IN_Y_RANGE );
    mouseClick.arm( event );

    mouseClick.trigger( event, action );

    verify( action ).run();
  }

  @Test
  public void triggerWithoutPrecedingMouseDown() {
    MouseEvent event = createLeftButtonMouseEventOnControlStub( X_RANGE, Y_RANGE, IN_X_RANGE, IN_Y_RANGE );

    mouseClick.trigger( event, action );

    verify( action, never() ).run();
  }

  @Test
  @Parameters( method = "provideOutOfRangeEvents" )
  public void triggerOnOutOfRangeEvents( MouseEvent event ) {
    mouseClick.arm( event );

    mouseClick.trigger( event, action );

    verify( action, never() ).run();
  }

  public static Object provideOutOfRangeEvents() {
    return $( $( createLeftButtonMouseEventOnControlStub( X_RANGE, Y_RANGE, -1, IN_Y_RANGE ) ),
              $( createLeftButtonMouseEventOnControlStub( X_RANGE, Y_RANGE, IN_X_RANGE, -1 ) ),
              $( createLeftButtonMouseEventOnControlStub( X_RANGE, Y_RANGE, X_RANGE + 1, IN_Y_RANGE ) ),
              $( createLeftButtonMouseEventOnControlStub( X_RANGE, Y_RANGE, IN_X_RANGE, Y_RANGE + 1 ) ) );
  }

  private static MouseEvent createLeftButtonMouseEventOnControlStub(
    int controlWidth, int controlHeight, int mouseX, int mouseY )
  {
    return createMouseEventOnControlStub( controlWidth, controlHeight, mouseX, mouseY, LEFT_BUTTON );
  }

  private static MouseEvent createRightButtonMouseEventOnControlStub(
    int controlWidth, int controlHeight, int mouseX, int mouseY )
  {
    return createMouseEventOnControlStub( controlWidth, controlHeight, mouseX, mouseY, 3 );
  }

  private static MouseEvent createMouseEventOnControlStub(
    int controlWidth, int controlHeight, int mouseX, int mouseY, int button )
  {
    Control control = stubControl( controlWidth, controlHeight );
    return createMouseEvent( control, mouseX, mouseY, SWT.DEFAULT, button );
  }

  private static Control stubControl( int width, int height ) {
    Control result = mock( Control.class );
    when( result.getSize() ).thenReturn( new Point( width, height ) );
    return result;
  }
}