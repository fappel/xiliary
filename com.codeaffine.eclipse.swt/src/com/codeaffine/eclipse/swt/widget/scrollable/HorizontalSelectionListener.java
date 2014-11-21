package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class HorizontalSelectionListener extends SelectionAdapter {

  private final Tree tree;

  HorizontalSelectionListener( Tree tree ) {
    this.tree = tree;
  }

  @Override
  public void widgetSelected( SelectionEvent event ) {
    Point location = tree.getLocation();
    tree.setLocation( - getSelection( event ), location.y );
  }

  private static int getSelection( SelectionEvent event ) {
    return ( ( FlatScrollBar )event.widget ).getSelection();
  }
}