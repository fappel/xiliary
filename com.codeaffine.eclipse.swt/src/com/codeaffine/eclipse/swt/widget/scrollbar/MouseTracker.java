package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Rectangle;

class MouseTracker extends MouseTrackAdapter implements Runnable {

  static final int DELAY = 500;

  private final FlatScrollBar scrollBar;

  private Rectangle bounds;
  private boolean mouseOver;

  MouseTracker( FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
    this.scrollBar.up.getControl().addMouseTrackListener( this );
    this.scrollBar.upFast.getControl().addMouseTrackListener( this );
    this.scrollBar.drag.getControl().addMouseTrackListener( this );
    this.scrollBar.downFast.getControl().addMouseTrackListener( this );
    this.scrollBar.down.getControl().addMouseTrackListener( this );
  }

  @Override
  public void mouseEnter( MouseEvent event ) {
    mouseOver = true;
    if( bounds == null ) {
      bounds = scrollBar.getControl().getBounds();
      scrollBar.getOrientation().expand( scrollBar.getControl() );
    }
  }

  @Override
  public void mouseExit( MouseEvent event ) {
    mouseOver = false;
    scrollBar.getControl().getDisplay().timerExec( DELAY, this );
  }

  @Override
  public void run() {
    if( !mouseOver && !scrollBar.getControl().isDisposed() ) {
      scrollBar.getControl().setBounds( bounds );
      bounds = null;
    }
  }
}