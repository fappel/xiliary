package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

class Overlay extends MouseAdapter implements ViewComponent, ControlListener {

  static final int ALPHA = 80;

  private final Composite toOverlay;
  private final Composite overlay;
  private final Shell parentShell;
  private final Control parent;

  Overlay( Composite toOverlay ) {
    this.parent = toOverlay.getParent();
    this.toOverlay = toOverlay;
    this.parentShell = parent.getShell();
    if( !"gtk".equals( SWT.getPlatform() ) ) {
      this.overlay = new Shell( parentShell, SWT.NO_TRIM | SWT.TOOL );
      overlay.setBackgroundMode( SWT.INHERIT_DEFAULT );
      parentShell.addControlListener( this );
      toOverlay.addControlListener( this );
      overlay.addMouseListener( this );
      overlay.setLayout( toOverlay.getLayout() );
      controlMoved( null );
      ( ( Shell )overlay ).setAlpha( ALPHA );
      ( ( Shell )overlay ).open();
    } else {
      this.overlay = toOverlay;
      this.overlay.addControlListener( new ControlAdapter() {
        @Override
        public void controlResized(ControlEvent e) {
          overlay.moveAbove( null );
        };
      } );
      overlay.moveAbove( null );
    }
  }

  public void keepParentShellActivated() {
    parent.getShell().setActive();
  }

  @Override
  public Composite getControl() {
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