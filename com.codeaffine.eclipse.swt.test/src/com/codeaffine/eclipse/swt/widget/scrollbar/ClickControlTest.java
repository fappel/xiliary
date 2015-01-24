package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.testhelper.MouseDownActionTimerHelper.waitTillMouseDownTimerHasBeenTriggered;
import static com.codeaffine.eclipse.swt.util.ButtonClick.LEFT_BUTTON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.ClickControl.ClickAction;

public class ClickControlTest {

  private static final int WIDTH = 100;
  private static final int HEIGHT = 200;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ClickControl clickControl;
  private ClickAction action;

  @Before
  public void setUp() {
    Shell shell = createShell( displayHelper, SWT.SHELL_TRIM );
    action = mock( ClickAction.class );
    clickControl = new ClickControl( shell, action, FlatScrollBar.DEFAULT_MAX_EXPANSION );
    shell.open();
  }

  @Test
  public void getControl() {
    Control control = clickControl.getControl();

    assertThat( control.getLayoutData() ).isNull();
  }

  @Test
  public void clickOnControl() {
    triggerLeftButtonMouseEvent( SWT.MouseDown );
    triggerLeftButtonMouseEvent( SWT.MouseUp );

    verify( action ).run();
  }

  @Test
  public void mouseUpOnly() {
    triggerLeftButtonMouseEvent( SWT.MouseUp );

    verify( action, never() ).run();
  }

  @Test
  public void mouseDownTimerActivation() {
    triggerLeftButtonMouseEvent( SWT.MouseDown );
    waitTillMouseDownTimerHasBeenTriggered();

    InOrder order = inOrder( action );
    order.verify( action, atLeastOnce() ).setCoordinates( 1, 1 );
    order.verify( action, atLeastOnce() ).run();
  }

  @Test
  public void mouseDownTimerDeactivation() {
    triggerLeftButtonMouseEvent( SWT.MouseDown );
    trigger( SWT.MouseExit ).on( clickControl.getControl() );
    waitTillMouseDownTimerHasBeenTriggered();

    verify( action, never() ).run();
  }

  @Test
  public void controlResized() {
    clickControl.getControl().setSize( WIDTH, HEIGHT );

    Image actual = clickControl.getControl().getImage();

    assertThat( actual.getBounds() ).isEqualTo( expectedImageBounds( WIDTH, HEIGHT ) );
  }

  @Test
  public void setColor() {
    ImageData first = renderImage( displayHelper.getDisplay().getSystemColor( SWT.COLOR_RED ) );
    ImageData second = renderImage( displayHelper.getDisplay().getSystemColor( SWT.COLOR_GREEN ) );

    assertThat( first.data ).isNotEqualTo( second.data );
  }

  @Test
  public void getColor() {
    Color expected = displayHelper.getDisplay().getSystemColor( SWT.COLOR_RED );

    clickControl.setColor( expected );
    Color actual = clickControl.getColor();

    assertThat( actual ).isSameAs( expected );
  }

  private ImageData renderImage( Color foreground ) {
    clickControl.setColor( foreground );
    clickControl.controlResized( null );
    return clickControl.getControl().getImage().getImageData();
  }

  private void triggerLeftButtonMouseEvent( int event ) {
    trigger( event ).at( 1, 1 ).withButton( LEFT_BUTTON ).on( clickControl.getControl() );
  }

  private static Rectangle expectedImageBounds( int width, int height ) {
    return new Rectangle( 0, 0, width, height );
  }
}