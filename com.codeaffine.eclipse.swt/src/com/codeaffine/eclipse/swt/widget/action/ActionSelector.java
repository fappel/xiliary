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
package com.codeaffine.eclipse.swt.widget.action;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.codeaffine.eclipse.swt.util.ButtonClick;

public class ActionSelector {

  private final Consumer<Updatable> updateWiring;
  private final BooleanSupplier enablement;
  private final ButtonClick buttonClick;
  private final Image enabledImage;
  private final Runnable action;

  private Image disabledImage;
  private Display display;
  private Label control;

  public ActionSelector( Runnable action, Image image, BooleanSupplier enablement, Consumer<Updatable> updateWiring ) {
    this.buttonClick = new ButtonClick();
    this.updateWiring = updateWiring;
    this.enablement = enablement;
    this.enabledImage = image;
    this.action = action;
  }

  public Control create( Composite parent ) {
    control = new Label( parent, SWT.CENTER );
    control.addListener( SWT.MouseDown, evt -> mouseDown( new MouseEvent( evt ) ) );
    control.addListener( SWT.MouseUp, evt -> mouseUp( new MouseEvent( evt ) ) );
    control.addListener( SWT.MouseEnter, evt -> mouseEnter() );
    control.addListener( SWT.MouseExit, evt -> mouseExit() );
    display = control.getDisplay();
    updateWiring.accept( () -> update() );
    update();
    return control;
  }

  private void mouseDown( MouseEvent event ) {
    if( enablement.getAsBoolean() ) {
      buttonClick.arm( event );
    }
  }

  private void mouseUp( MouseEvent event ) {
    if( enablement.getAsBoolean() ) {
      buttonClick.trigger( event, action );
    }
  }

  public void mouseEnter() {
    if( enablement.getAsBoolean() ) {
      control.setBackground( control.getDisplay().getSystemColor( SWT.COLOR_LIST_SELECTION ) );
    }
  }

  public void mouseExit() {
    control.setBackground( null );
  }

  private void update() {
    display.syncExec( () -> control.setImage( getImage() ) );
  }

  private Image getImage() {
    if( !enablement.getAsBoolean() ) {
      ensureDisableImage();
      return disabledImage;
    }
    return enabledImage;
  }

  private void ensureDisableImage() {
    if( disabledImage == null ) {
      disabledImage = new Image( control.getDisplay(), enabledImage, SWT.IMAGE_GRAY );
      control.addListener( SWT.Dispose, evt -> disabledImage.dispose() );
    }
  }
}