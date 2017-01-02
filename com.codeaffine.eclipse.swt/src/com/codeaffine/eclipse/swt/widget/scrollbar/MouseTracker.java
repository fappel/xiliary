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

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

class MouseTracker extends MouseTrackAdapter implements Runnable, DisposeListener {

  static final int DELAY = 500;

  private final FlatScrollBar scrollBar;
  private final int maxExpansion;

  private Rectangle expandedBounds;
  private Rectangle originBounds;
  private boolean mouseOver;
  private boolean disposed;

  MouseTracker( FlatScrollBar scrollBar, int maxExpansion  ) {
    this.scrollBar = scrollBar;
    this.maxExpansion = maxExpansion;
    this.scrollBar.addDisposeListener( this );
    this.scrollBar.up.getControl().addMouseTrackListener( this );
    this.scrollBar.upFast.getControl().addMouseTrackListener( this );
    this.scrollBar.drag.getControl().addMouseTrackListener( this );
    this.scrollBar.downFast.getControl().addMouseTrackListener( this );
    this.scrollBar.down.getControl().addMouseTrackListener( this );
  }

  @Override
  public void mouseEnter( MouseEvent event ) {
    mouseOver = true;
    if( !disposed && originBounds == null ) {
      originBounds = scrollBar.getBounds();
      scrollBar.getDirection().expand( scrollBar, maxExpansion );
      expandedBounds = scrollBar.getBounds();
    }
  }

  @Override
  public void mouseExit( MouseEvent event ) {
    mouseOver = false;
    if( !disposed ) {
      Display.getCurrent().timerExec( DELAY, this );
    }
  }

  @Override
  public void run() {
    if( !disposed && !mouseOver ) {
      if( scrollBar.getBounds().equals( expandedBounds ) ) {
        scrollBar.setBounds( originBounds );
      }
      originBounds = null;
      expandedBounds = null;
    }
  }

  @Override
  public void widgetDisposed( DisposeEvent e ) {
    disposed = true;
  }
}