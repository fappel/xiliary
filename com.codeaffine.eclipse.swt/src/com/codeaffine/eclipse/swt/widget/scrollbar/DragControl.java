package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.codeaffine.eclipse.swt.util.DragDetector;

class DragControl
  extends ControlAdapter
  implements ViewComponent, DragDetectListener, MouseListener, MouseMoveListener
{

  private final DragImageUpdate dragImageUpdate;
  private final DragDetector dragDetector;
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
    this.dragImageUpdate = new DragImageUpdate( control, maxExpansion );
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
    dragImageUpdate.update();
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