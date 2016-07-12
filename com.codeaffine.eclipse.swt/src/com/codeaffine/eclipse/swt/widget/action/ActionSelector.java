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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.codeaffine.eclipse.swt.util.ButtonClick;

public class ActionSelector {

  private final ButtonClick buttonClick;
  private final Runnable action;
  private final Image image;

  private Label control;

  public ActionSelector( Runnable action, Image image ) {
    this.buttonClick = new ButtonClick();
    this.action = action;
    this.image = image;
  }

  public Control create( Composite parent ) {
    control = new Label( parent, SWT.CENTER );
    control.setImage( image );
    control.addListener( SWT.MouseDown, evt -> mouseDown( new MouseEvent( evt ) ) );
    control.addListener( SWT.MouseUp, evt -> mouseUp( new MouseEvent( evt ) ) );
    control.addListener( SWT.MouseEnter, evt -> mouseEnter() );
    control.addListener( SWT.MouseExit, evt -> mouseExit() );
    return control;
  }

  private void mouseDown( MouseEvent event ) {
    buttonClick.arm( event );
  }

  private void mouseUp( MouseEvent event ) {
    buttonClick.trigger( event, action );
  }

  public void mouseEnter() {
    control.setBackground( control.getDisplay().getSystemColor( SWT.COLOR_LIST_SELECTION ) );
  }

  public void mouseExit() {
    control.setBackground( null );
  }
}