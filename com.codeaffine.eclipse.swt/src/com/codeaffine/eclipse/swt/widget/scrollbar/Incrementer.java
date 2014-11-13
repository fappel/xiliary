package com.codeaffine.eclipse.swt.widget.scrollbar;

import com.codeaffine.eclipse.swt.widget.scrollbar.ClickControl.ClickAction;

class Incrementer implements ClickAction {

  private final FlatScrollBar scrollBar;

  Incrementer( FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
  }

  @Override
  public void run() {
    scrollBar.setSelectionInternal( scrollBar.getSelection() + scrollBar.getIncrement() );
  }

  @Override
  public void setCoordinates( int x, int y ) {
  }
}