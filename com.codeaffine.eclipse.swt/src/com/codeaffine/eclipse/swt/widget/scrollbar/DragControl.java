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
package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.codeaffine.eclipse.swt.util.DragDetector;

class DragControl
  extends ControlAdapter
  implements ViewComponent, DragDetectListener, MouseListener, MouseMoveListener
{

  private final DragDetector dragDetector;
  private final ImageUpdate imageUpdate;
  private final DragAction dragAction;
  private final Label control;

  private Point startingPosition;

  public interface DragAction {
    void start();
    void run( int startX, int startY, int currentX, int currentY );
    void end();
  }

  DragControl( Composite parent, DragAction dragAction, int maxExpansion ) {
    this.control = new Label( parent, SWT.NONE );
    this.imageUpdate = new ImageUpdate( control, maxExpansion );
    this.dragDetector = new DragDetector( control, 0 );
    this.dragAction = dragAction;
    initializeControl();
  }

  @Override
  public Label getControl() {
    return control;
  }

  @Override
  public void dragDetected( DragDetectEvent event ) {
    if( startingPosition != null ) {
      dragAction.run( startingPosition.x, startingPosition.y, event.x, event.y );
    }
    dragDetector.dragHandled();
  }

  @Override
  public void mouseDown( MouseEvent event ) {
    startingPosition = new Point( event.x, event.y );
    dragAction.start();
  }

  @Override
  public void mouseUp( MouseEvent event ) {
    if( startingPosition != null ) {
      dragAction.end();
    }
    startingPosition = null;
  }

  @Override
  public void mouseMove( MouseEvent event ) {
    dragDetector.mouseMove( event );
  }

  @Override
  public void controlResized( ControlEvent event ) {
    imageUpdate.update();
  }

  void setForeground( Color color ) {
    imageUpdate.setForeground( color );
  }

  Color getForeground() {
    return imageUpdate.getForeground();
  }

  Color getBackground() {
    return imageUpdate.getBackground();
  }

  void setBackground( Color color ) {
    imageUpdate.setBackground( color );
  }

  private void initializeControl( ) {
    control.addMouseListener( this );
    control.addMouseMoveListener( this );
    control.addControlListener( this );
    control.addDragDetectListener( this );
  }

  @Override
  public void mouseDoubleClick( MouseEvent event ) {}
}