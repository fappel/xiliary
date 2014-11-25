package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;

class ResizeObserver extends ControlAdapter {

  private final FlatScrollBar flatScrollBar;

  public ResizeObserver( FlatScrollBar flatScrollBar ) {
    this.flatScrollBar = flatScrollBar;
  }

  @Override
  public void controlResized( ControlEvent event ) {
    flatScrollBar.layout();
    flatScrollBar.moveAbove( null );
  }
}