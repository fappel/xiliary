package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class HorizontalSelectionListener extends SelectionAdapter {

  private final LayoutContext<?> context;

  HorizontalSelectionListener( LayoutContext<?> context ) {
    this.context = context;
  }

  @Override
  public void widgetSelected( SelectionEvent event ) {
    Point location = context.getScrollable().getLocation();
    Point mappedLocation = Display.getCurrent().map( context.getScrollable(), context.getAdapter(), location );
    Point result = new Point( - getSelection( event ), location.y - mappedLocation.y );
    context.getScrollable().setLocation( result );
  }

  private static int getSelection( SelectionEvent event ) {
    return ( ( FlatScrollBar )event.widget ).getSelection();
  }
}