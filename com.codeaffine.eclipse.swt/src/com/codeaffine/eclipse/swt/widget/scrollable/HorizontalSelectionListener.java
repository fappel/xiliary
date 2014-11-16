package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.widget.scrollbar.ScrollEvent;
import com.codeaffine.eclipse.swt.widget.scrollbar.ScrollListener;

class HorizontalSelectionListener implements ScrollListener {

  private final Tree tree;

  HorizontalSelectionListener( Tree tree ) {
    this.tree = tree;
  }

  @Override
  public void selectionChanged( ScrollEvent event ) {
    Point location = tree.getLocation();
    tree.setLocation( - event.getSelection(), location.y );
  }
}