package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.DragControl.DragAction;

public class DragControlTest {

  private static final int WIDTH = 100;
  private static final int HEIGHT = 200;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private DragControl clickControl;
  private DragAction dragAction;
  private Shell parent;

  @Before
  public void setUp() {
    parent = displayHelper.createShell( SWT.SHELL_TRIM );
    dragAction = mock( DragAction.class );
    clickControl = new DragControl( parent, dragAction, FlatScrollBar.DEFAULT_MAX_EXPANSION );
  }

  @Test
  public void dragDetection() {
    trigger( SWT.MouseDown ).at( 1, 1 ).on( getControl() );
    trigger( SWT.MouseMove ).at( 10, 10 ).withStateMask( SWT.BUTTON1 ).on( getControl() );

    InOrder order = inOrder( dragAction );
    order.verify( dragAction ).start();
    order.verify( dragAction ).run( 1, 1, 10, 10 );
    order.verifyNoMoreInteractions();
  }

  @Test
  public void dragDetectionIfMouseDownNotOnControl() {
    trigger( SWT.MouseDown ).at( 1, 1 ).on( parent );
    trigger( SWT.MouseMove ).at( 10, 10 ).withStateMask( SWT.BUTTON1 ).on( getControl() );

    verifyNoMoreInteractions( dragAction );
  }

  @Test
  public void dragDetectionWithConsecutiveEvents() {
    trigger( SWT.MouseDown ).at( 1, 1 ).on( getControl() );
    trigger( SWT.MouseMove ).at( 10, 10 ).withStateMask( SWT.BUTTON1 ).on( getControl() );
    trigger( SWT.MouseMove ).at( 20, 20 ).withStateMask( SWT.BUTTON1 ).on( getControl() );

    InOrder order = inOrder( dragAction );
    order.verify( dragAction ).start();
    order.verify( dragAction ).run( 1, 1, 10, 10 );
    order.verify( dragAction ).run( 1, 1, 20, 20 );
    order.verifyNoMoreInteractions();
  }

  @Test
  public void dragDetectionAfterMouseUp() {
    trigger( SWT.MouseDown ).at( 1, 1 ).on( getControl() );
    trigger( SWT.MouseMove ).at( 10, 10 ).withStateMask( SWT.BUTTON1 ).on( getControl() );
    trigger( SWT.MouseUp ).at( 10, 10 ).on( getControl() );
    trigger( SWT.MouseMove ).at( 20, 20 ).withStateMask( SWT.BUTTON1 ).on( getControl() );

    InOrder order = inOrder( dragAction );
    order.verify( dragAction ).start();
    order.verify( dragAction ).run( 1, 1, 10, 10 );
    order.verify( dragAction ).end();
    order.verifyNoMoreInteractions();
  }
  @Test
  public void mouseUp() {
    trigger( SWT.MouseUp ).at( 10, 10 ).on( getControl() );

    verifyNoMoreInteractions( dragAction );
  }

  @Test
  public void controlResized() {
    getControl().setSize( WIDTH, HEIGHT );

    Image actual = getControl().getImage();

    assertThat( actual.getBounds() ).isEqualTo( expectedImageBounds( WIDTH, HEIGHT ) );
  }

  @Test
  public void testGetControl() {
    Control control = clickControl.getControl();

    assertThat( control.getLayoutData() ).isNull();
  }

  private Label getControl() {
    return clickControl.getControl();
  }

  private static Rectangle expectedImageBounds( int width, int height ) {
    return new Rectangle( 0, 0, width, height );
  }
}