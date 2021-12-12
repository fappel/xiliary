/**
 * Copyright (c) 2014 - 2022 Frank Appel
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
import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.testhelper.MouseDownActionTimerHelper.waitTillMouseDownTimerHasBeenTriggered;
import static com.codeaffine.eclipse.swt.util.ButtonClick.LEFT_BUTTON;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.equipScrollBarWithListener;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBarHelper.verifyNotification;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

@RunWith( value = Parameterized.class )
public class FastDecrementerTest {

  @Rule
  public final ConditionalIgnoreRule conditionalIgnore = new ConditionalIgnoreRule();

  @Parameters
  public static Collection<Object[]> data() {
    return DirectionHelper.valuesForParameterizedTests();
  }

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private final int direction;

  private FlatScrollBar scrollBar;

  public FastDecrementerTest( int direction ) {
    this.direction = direction;
  }

  @Before
  public void setUp() {
    Shell shell = createShell( displayHelper, SWT.SHELL_TRIM );
    scrollBar = new FlatScrollBar( shell, direction );
    shell.open();
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void run() {
    scrollBar.setSelectionInternal( scrollBar.getPageIncrement() * 2, SWT.PAGE_DOWN );
    Point size = getUpFastControl().getSize();
    SelectionListener listener = equipScrollBarWithListener( scrollBar );

    triggerLeftButtonMouseDown( size );
    waitTillTimerHasFiredAtLeastTwice();
    triggerMouseUp();
    flushPendingEvents();

    SelectionEvent event = verifyNotification( listener );
    assertThat( event.detail ).isEqualTo( SWT.PAGE_UP );
    assertThat( scrollBar.getSelection() ).isEqualTo( scrollBar.getPageIncrement() );
  }

  private void triggerLeftButtonMouseDown( Point size ) {
    trigger( SWT.MouseDown ).at( size.x, size.y ).withButton( LEFT_BUTTON ).on( getUpFastControl() );
  }

  private void triggerMouseUp() {
    trigger( SWT.MouseUp ).at( 1, 1 ).on( getUpFastControl() );
  }

  private Control getUpFastControl() {
    return scrollBar.upFast.getControl();
  }

  private static void waitTillTimerHasFiredAtLeastTwice() {
    waitTillMouseDownTimerHasBeenTriggered();
    waitTillMouseDownTimerHasBeenTriggered();
  }
}