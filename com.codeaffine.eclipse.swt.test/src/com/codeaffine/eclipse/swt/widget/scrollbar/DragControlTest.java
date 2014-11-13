package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollbar.DragControl.DragAction;

public class DragControlTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private DragControl clickControl;
  private DragAction dragAction;

  @Before
  public void setUp() {
    Shell parent = displayHelper.createShell( SWT.SHELL_TRIM );
    dragAction = mock( DragAction.class );
    clickControl = new DragControl( parent, getSystemColorRed(), dragAction );
  }

  @Test
  public void dragDetection() {
    trigger( SWT.MouseDown ).at( 1, 1 ).on( getControl() );
    trigger( SWT.MouseMove ).at( 10, 10 ).withStateMask( SWT.BUTTON1 ).on( getControl() );

    verify( dragAction ).run( 1, 1, 10, 10 );
  }

  @Test
  public void dragDetectionWithConsecutiveEvents() {
    trigger( SWT.MouseDown ).at( 1, 1 ).on( getControl() );
    trigger( SWT.MouseMove ).at( 10, 10 ).withStateMask( SWT.BUTTON1 ).on( getControl() );
    trigger( SWT.MouseMove ).at( 20, 20 ).withStateMask( SWT.BUTTON1 ).on( getControl() );

    verify( dragAction ).run( 1, 1, 10, 10 );
    verify( dragAction ).run( 1, 1, 20, 20 );
  }

  @Test
  public void dragDetectionAfterMouseUp() {
    trigger( SWT.MouseDown ).at( 1, 1 ).on( getControl() );
    trigger( SWT.MouseMove ).at( 10, 10 ).withStateMask( SWT.BUTTON1 ).on( getControl() );
    trigger( SWT.MouseUp ).at( 10, 10 ).on( getControl() );
    trigger( SWT.MouseMove ).at( 20, 20 ).withStateMask( SWT.BUTTON1 ).on( getControl() );

    verify( dragAction ).run( 1, 1, 10, 10 );
    verify( dragAction, never() ).run( 1, 1, 20, 20 );
  }

  @Test
  public void testGetControl() {
    Control control = clickControl.getControl();

    assertThat( control.getBackground() ).isEqualTo( getSystemColorRed() );
    assertThat( control.getLayoutData() ).isNull();
  }

  private Control getControl() {
    return clickControl.getControl();
  }

  private static Color getSystemColorRed() {
    return Display.getCurrent().getSystemColor( SWT.COLOR_RED );
  }
}