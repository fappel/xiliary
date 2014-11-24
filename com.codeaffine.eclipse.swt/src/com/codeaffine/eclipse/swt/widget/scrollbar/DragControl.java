package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.codeaffine.eclipse.swt.util.DragDetector;

class DragControl extends ControlAdapter implements ViewComponent, DragDetectListener, DisposeListener, Listener {

  private final DragImageUpdate dragImageUpdate;
  private final DragDetector dragDetector;
  private final DragAction dragAction;
  private final Overlay overlay;
  private final Label control;

  private Point startingPosition;

  public interface DragAction {
    void start();
    void run( int startX, int startY, int currentX, int currentY );
    void end();
  }

  DragControl( Overlay overlay, DragAction dragAction, int maxExpansion ) {
    this.control = new Label( overlay.getControl(), SWT.NONE );
    this.dragImageUpdate = new DragImageUpdate( control, maxExpansion );
    this.dragDetector = new DragDetector( control, 0 );
    this.dragAction = dragAction;
    this.overlay = overlay;
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
  public void handleEvent( Event event ) {
    if( event.type == SWT.MouseDown ) {
      if( event.widget == control ) {
        mouseDown( event );
        overlay.keepParentShellActivated();
      }
    } else if( event.type == SWT.MouseMove ) {
      mouseMove( event );
    } else if( event.type == SWT.MouseUp ) {
      mouseUp();
    }
  }

  @Override
  public void controlResized( ControlEvent event ) {
    dragImageUpdate.update();
  }

  @Override
  public void widgetDisposed( DisposeEvent e ) {
    control.getDisplay().removeFilter( SWT.MouseMove, this );
    control.getDisplay().removeFilter( SWT.MouseDown, this );
    control.getDisplay().removeFilter( SWT.MouseUp, this );
  }

  private void initializeControl( ) {
    control.getDisplay().addFilter( SWT.MouseMove, this );
    control.getDisplay().addFilter( SWT.MouseDown, this );
    control.getDisplay().addFilter( SWT.MouseUp, this );
    control.addControlListener( this );
    control.addDragDetectListener( this );
    control.addDisposeListener( this );
  }

  private void mouseDown( Event event ) {
    startingPosition = new Point( event.x, event.y );
    dragAction.start();
  }

  private void mouseMove( Event event ) {
    dragDetector.mouseMove( new MouseEvent( event ) );
  }

  private void mouseUp() {
    if( startingPosition != null ) {
      dragAction.end();
    }
    startingPosition = null;
  }
}