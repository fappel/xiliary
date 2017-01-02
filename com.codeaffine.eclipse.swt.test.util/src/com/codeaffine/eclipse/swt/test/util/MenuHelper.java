/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.test.util;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;

import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;

public class MenuHelper {

  public static final int DELAY = 30;

  private final Display display;

  public MenuHelper( Display display ) {
    this.display = display;
  }

  public Menu simulateItemSelection( Control control, Function<Control, Menu> menuCreator, int selectionIndex ) {
    timerExec( () -> triggerItemSelection( control, selectionIndex ) );
    return createMenu( control, menuCreator );
  }

  public Menu createMenu( Control control, Function<Control, Menu> menuCreator ) {
    Menu result = menuCreator.apply( control );
    control.setMenu( result );
    result.setVisible( true );
    flushPendingEvents();
    return result;
  }

  private void timerExec( Runnable runnable ) {
    display.timerExec( DELAY, runnable );
  }

  private static void triggerItemSelection( Control control, int itemIndex ) {
    Menu menu = control.getMenu();
    trigger( SWT.Selection ).on( menu.getItem( itemIndex ) );
    menu.setVisible( false );
  }
}