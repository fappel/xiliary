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

import static com.codeaffine.eclipse.swt.test.util.SWTEventHelper.trigger;
import static com.codeaffine.eclipse.swt.test.util.UiThreadHelper.runInOwnThreadWithReadAndDispatch;
import static com.codeaffine.eclipse.swt.widget.action.EnablementHelper.configureAsDisabled;
import static com.codeaffine.eclipse.swt.widget.action.EnablementHelper.configureAsEnabled;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.util.ButtonClick;

public class ActionSelectorTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private Consumer<Updatable> updateWiring;
  private BooleanSupplier enablement;
  private ActionSelector selector;
  private Updatable updatable;
  private Runnable action;
  private Image image;

  @Before
  public void setUp() {
    action = mock( Runnable.class );
    enablement = configureAsEnabled( mock( BooleanSupplier.class ) );
    updateWiring = updatable -> this.updatable = updatable;
    image = displayHelper.createImage( 1, 1 );
    selector = new ActionSelector( action, image, enablement, updateWiring );
  }

  @Test
  public void create() {
    Label control = ( Label )selector.create( displayHelper.createShell() );

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

    verify( action ).run();
  }

  @Test
  public void mouseClickIfDisabled() {
    configureAsDisabled( enablement );
    Control control = selector.create( displayHelper.createShell() );

    trigger( SWT.MouseDown ).withButton( ButtonClick.LEFT_BUTTON ).on( control );
    trigger( SWT.MouseUp ).on( control );

    verify( action, never() ).run();
  }

  @Test
  public void dispose() {
    configureAsDisabled( enablement );
    Control control = selector.create( displayHelper.createShell() );
    Image disabledImage = ( ( Label )control ).getImage();

    control.dispose();

    assertThat( disabledImage.isDisposed() );
  }

  @Test
  public void disable() {
    Control control = selector.create( displayHelper.createShell() );
    configureAsDisabled( enablement );

    updatable.update();

    assertThat( ( ( Label )control ).getImage() ).isNotSameAs( image );
  }

  @Test
  public void disableFromBackgroundThread() {
    Shell shell = displayHelper.createShell();
    Control control = selector.create( shell );
    configureAsDisabled( enablement );

    runInOwnThreadWithReadAndDispatch( () -> updatable.update() );

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
    configureAsEnabled( enablement );

    updatable.update();

    assertThat( ( ( Label )control ).getImage() ).isSameAs( image );
  }
}