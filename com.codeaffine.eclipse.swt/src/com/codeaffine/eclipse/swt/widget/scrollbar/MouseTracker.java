package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Rectangle;

class MouseTracker extends MouseTrackAdapter implements Runnable {

  static final int DELAY = 500;

  private final FlatScrollBar scrollBar;

  private Rectangle expandedBounds;
  private boolean mouseOver;
  private Rectangle originBounds;


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
    if( originBounds == null ) {
      originBounds = scrollBar.getBounds();
      scrollBar.getDirection().expand( scrollBar );
      expandedBounds = scrollBar.getBounds();
    }
  }

  @Override
  public void mouseExit( MouseEvent event ) {
    mouseOver = false;
    scrollBar.getDisplay().timerExec( DELAY, this );
  }

  @Override
  public void run() {
    if( !mouseOver && !scrollBar.isDisposed() ) {
      if( scrollBar.getBounds().equals( expandedBounds ) ) {
        scrollBar.setBounds( originBounds );
      }
      originBounds = null;
      expandedBounds = null;
    }
  }
}