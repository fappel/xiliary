package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.codeaffine.eclipse.swt.util.DragDetector;

class DragControl
  implements ViewComponent, MouseListener, MouseMoveListener, DragDetectListener
{

  private final Label control;
  private final DragDetector dragDetector;
  private final DragAction dragAction;

  private Point startingPosition;

  public interface DragAction {
    void run( int startX, int startY, int currentX, int currentY );
  }

  DragControl( Composite parent, Color background, DragAction dragAction ) {
    this.control = new Label( parent, SWT.NONE );
    this.dragDetector = new DragDetector( control, 0 );
    this.dragAction = dragAction;
    initializeControl( background );
  }

  @Override
  public Control getControl() {
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
  public void mouseMove( MouseEvent event ) {
    dragDetector.mouseMove( event );
  }

  @Override
  public void mouseDoubleClick( MouseEvent e ) {
  }

  @Override
  public void mouseDown( MouseEvent event ) {
    this.startingPosition = new Point( event.x, event.y );
  }

  @Override
  public void mouseUp( MouseEvent e ) {
    this.startingPosition = null;
  }

  private void initializeControl( Color background ) {
    control.setBackground( background );
    control.addMouseListener( this );
    control.addMouseMoveListener( this );
    control.addDragDetectListener( this );
  }
}