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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.function.Function;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.MenuHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class MenuCreatorFactoryTest {

  private static final String LABEL = "label";
  private static final int ITEM_INDEX = 0;

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();
  @Rule public final ConditionalIgnoreRule conditionIgnore = new ConditionalIgnoreRule();

  private MenuCreatorFactory factory;
  private MenuHelper menuHelper;

  @Before
  public void setUp() {
    factory = new MenuCreatorFactory();
    menuHelper = new MenuHelper( displayHelper.getDisplay() );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class)
  public void createShowAndSelectItem() {
    Listener itemListener = mock( Listener.class );

    Function<Control, Menu> creator = factory.create( menu -> createPushItem( menu, LABEL, itemListener ) );
    Menu actual = menuHelper.simulateItemSelection( displayHelper.createShell(), creator, ITEM_INDEX );

    assertThat( actual.getItemCount() ).isEqualTo( 1 );
    assertThat( actual.getItem( 0 ).getText() ).isEqualTo( LABEL );
    verify( itemListener ).handleEvent( any( Event.class ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class)
  public void createShowAndSelectTwice() {
    Listener itemListener = mock( Listener.class );
    Function<Control, Menu> creator = factory.create( menu -> createPushItem( menu, LABEL, itemListener ) );
    Shell control = displayHelper.createShell();

    Menu menu = menuHelper.simulateItemSelection( control, creator, ITEM_INDEX );
    MenuItem itemOfFirstSelection = menu.getItem( 0 );
    menuHelper.simulateItemSelection( control, parent -> menu, ITEM_INDEX );

    assertThat( menu.getItemCount() ).isEqualTo( 1 );
    assertThat( menu.getItem( 0 ).getText() ).isEqualTo( LABEL );
    assertThat( menu.getItem( 0 ) ).isNotSameAs( itemOfFirstSelection );
    assertThat( itemOfFirstSelection.isDisposed() ).isTrue();
    verify( itemListener, times( 2 ) ).handleEvent( any( Event.class ) );
  }

  private static void createPushItem( Menu menu, String label, Listener itemListener ) {
    new MenuItemFactory().createPushItem( menu, label, itemListener );
  }
}