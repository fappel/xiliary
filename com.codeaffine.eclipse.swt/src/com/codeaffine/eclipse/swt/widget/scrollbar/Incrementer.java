package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.SWT;

import com.codeaffine.eclipse.swt.widget.scrollbar.ClickControl.ClickAction;

class Incrementer implements ClickAction {

  private final FlatScrollBar scrollBar;

  Incrementer( FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
  }

  @Override
  public void run() {
    int selection = scrollBar.getSelection() + scrollBar.getIncrement();
    scrollBar.setSelectionInternal( selection, SWT.ARROW_DOWN );
  }

  @Override
  public void setCoordinates( int x, int y ) {
  }
}