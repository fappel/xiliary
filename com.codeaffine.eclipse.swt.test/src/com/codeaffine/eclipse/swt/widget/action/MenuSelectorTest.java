/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.action;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static com.codeaffine.eclipse.swt.widget.action.EnablementHelper.configureAsDisabled;
import static com.codeaffine.eclipse.swt.widget.action.EnablementHelper.configureAsEnabled;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ButtonClick;

public class MenuSelectorTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Function<Control, Menu> menuCreator;
  private Consumer<Updatable> updateWiring;
  private BooleanSupplier enablement;
  private MenuSelector selector;
  private Updatable updatable;
  private Image image;

  @Before
  public void setUp() {
    menuCreator = control -> new Menu( control );
    enablement = configureAsEnabled( mock( BooleanSupplier.class ) );
    updateWiring = updatable -> this.updatable = updatable;
    image = displayHelper.createImage( 1, 1 );
    selector = new MenuSelector( menuCreator, image, enablement, updateWiring );
  }

  @Test
  public void create() {
    Label control = ( Label )selector.create( displayHelper.createShell() );
    flushPendingEvents();

    assertThat( control ).isNotNull();
    assertThat( control.getImage() ).isSameAs( image );
  }

  @Test
  public void createIfDisabled() {
    configureAsDisabled( enablement );

    Label control = ( Label )selector.create( displayHelper.createShell() );

    assertThat( control ).isNotNull();
    assertThat( control.getImage() ).isNotSameAs( image );
  }

  @Test
  public void dispose() {
    configureAsDisabled( enablement );
    Control control = selector.create( displayHelper.createShell() );
    flushPendingEvents();
    Image disabledImage = ( ( Label )control ).getImage();
    Menu menu = control.getMenu();

    control.dispose();

    assertThat( menu.isDisposed() ).isTrue();
    assertThat( disabledImage.isDisposed() );
  }

  @Test
  public void mouseEnter() {
    Control control = selector.create( displayHelper.createShell() );

    trigger( SWT.MouseEnter ).on( control );

    assertThat( control.getBackground().getRGB() )
      .isEqualTo( displayHelper.getSystemColor( SWT.COLOR_LIST_SELECTION ).getRGB() );
  }

  @Test
  public void mouseEnterIfDisabled() {
    configureAsDisabled( enablement );
    Control control = selector.create( displayHelper.createShell() );

    trigger( SWT.MouseEnter ).on( control );

    assertThat( control.getBackground().getRGB() )
      .isNotEqualTo( displayHelper.getSystemColor( SWT.COLOR_LIST_SELECTION ).getRGB() );
  }

  @Test
  public void mouseEnterAndExit() {
    Control control = selector.create( displayHelper.createShell() );
    Color expected = control.getBackground();

    trigger( SWT.MouseEnter ).on( control );
    trigger( SWT.MouseExit ).on( control );

    assertThat( control.getBackground().getRGB() )
      .isEqualTo( expected.getRGB() );
  }

  @Test
  public void mouseEnterAndExitIfDisabledAfterMouseEnter() {
    Control control = selector.create( displayHelper.createShell() );
    Color expected = control.getBackground();

    trigger( SWT.MouseEnter ).on( control );
    configureAsDisabled( enablement );
    trigger( SWT.MouseExit ).on( control );

    assertThat( control.getBackground().getRGB() )
      .isEqualTo( expected.getRGB() );
  }

  @Test
  public void mouseClick() {
    Control control = selector.create( displayHelper.createShell() );

    trigger( SWT.MouseDown ).withButton( ButtonClick.LEFT_BUTTON ).on( control );
    trigger( SWT.MouseUp ).on( control );

    assertThat( control.getMenu().isVisible() ).isTrue();
  }

  @Test
  public void mouseClickIfDisabled() {
    configureAsDisabled( enablement );
    Control control = selector.create( displayHelper.createShell() );

    trigger( SWT.MouseDown ).withButton( ButtonClick.LEFT_BUTTON ).on( control );
    trigger( SWT.MouseUp ).on( control );

    assertThat( control.getMenu().isVisible() ).isFalse();
  }

  @Test
  public void disable() {
    Control control = selector.create( displayHelper.createShell() );
    configureAsDisabled( enablement );

    updatable.update();

    assertThat( ( ( Label )control ).getImage() ).isNotSameAs( image );
  }

  @Test
  public void disableTwice() {
    Control control = selector.create( displayHelper.createShell() );
    configureAsDisabled( enablement );

    updatable.update();
    Image disableImage = ( ( Label )control ).getImage();
    updatable.update();

    assertThat( disableImage ).isSameAs( ( ( Label )control ).getImage() );
  }

  @Test
  public void enable() {
    configureAsDisabled( enablement );
    Control control = selector.create( displayHelper.createShell() );
    flushPendingEvents();
    configureAsEnabled( enablement );

    updatable.update();
    flushPendingEvents();

    assertThat( ( ( Label )control ).getImage() ).isSameAs( image );
  }
}