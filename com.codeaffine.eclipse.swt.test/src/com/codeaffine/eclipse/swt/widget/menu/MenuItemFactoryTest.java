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
package com.codeaffine.eclipse.swt.widget.menu;

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class MenuItemFactoryTest {

  private static final String LABEL = "label";

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private MenuItemFactory menuItemFactory;
  private Menu menu;

  @Before
  public void setUp() {
    Shell shell = createShell( displayHelper );
    menu = new Menu( shell );
    menuItemFactory = new MenuItemFactory();
  }

  @Test
  public void createPushItem() {
    menuItemFactory.createPushItem( menu, LABEL, mock( Listener.class ) );

    assertThat( menu.getItemCount() ).isEqualTo( 1 );
    assertThat( menu.getItem( 0 ).getText() ).isEqualTo( LABEL );
  }

  @Test
  public void selectPushItem() {
    Listener listener = mock( Listener.class );
    menuItemFactory.createPushItem( menu, LABEL, listener );

    trigger( SWT.Selection ).on( menu.getItem( 0 ) );

    verify( listener ).handleEvent( any( Event.class ) );
  }

  @Test
  public void createSeparator() {
    menuItemFactory.createSeparator( menu );

    assertThat( menu.getItemCount() ).isEqualTo( 1 );
    assertThat( menu.getItem( 0 ).getStyle() & SWT.SEPARATOR ).isEqualTo( SWT.SEPARATOR );
  }
}