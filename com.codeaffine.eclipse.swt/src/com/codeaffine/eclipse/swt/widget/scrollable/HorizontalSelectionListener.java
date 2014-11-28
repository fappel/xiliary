package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class HorizontalSelectionListener extends SelectionAdapter {

  private final Scrollable scrollable;

  HorizontalSelectionListener( Scrollable scrollable ) {
    this.scrollable = scrollable;
  }

  @Override
  public void widgetSelected( SelectionEvent event ) {
    Point location = scrollable.getLocation();
    scrollable.setLocation( - getSelection( event ), location.y );
  }

  private static int getSelection( SelectionEvent event ) {
    return ( ( FlatScrollBar )event.widget ).getSelection();
  }
}