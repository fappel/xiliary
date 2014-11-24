package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

class Overlay extends MouseAdapter implements ViewComponent, ControlListener {

  static final int ALPHA = 80;

  private final Shell parentShell;
  private final Control toOverlay;
  private final Control parent;
  private final Shell overlay;

  Overlay( Control toOverlay ) {
    this.parent = toOverlay.getParent();
    this.toOverlay = toOverlay;
    this.parentShell = parent.getShell();
    this.overlay = new Shell( parentShell, SWT.NO_TRIM );
    overlay.setAlpha( ALPHA );
    overlay.setBackgroundMode( SWT.INHERIT_DEFAULT );
    parentShell.addControlListener( this );
    toOverlay.addControlListener( this );
    overlay.addMouseListener( this );
    controlMoved( null );
    overlay.open();
  }

  public void keepParentShellActivated() {
    parent.getShell().setActive();
  }

  @Override
  public Shell getControl() {
    return overlay;
  }

  @Override
  public void mouseDown( MouseEvent event ) {
    keepParentShellActivated();
  }

  @Override
  public void controlMoved( ControlEvent event ) {
    parent.getLocation();
    Point location = Display.getCurrent().map( parent, null, toOverlay.getLocation() );
    overlay.setLocation( location );
  }

  @Override
  public void controlResized( ControlEvent event ) {
    overlay.setSize( toOverlay.getSize() );
  }
}