package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.widget.scrollbar.ScrollEvent;
import com.codeaffine.eclipse.swt.widget.scrollbar.ScrollListener;

class VerticalSelectionListener implements ScrollListener {

  private final TreeTopItemSelector treeTopItemSelector;

  VerticalSelectionListener( Tree tree ) {
    this.treeTopItemSelector = new TreeTopItemSelector( ( tree ) );
  }

  @Override
  public void selectionChanged( ScrollEvent event ) {
    int increment = event.getScrollBar().getIncrement();
    int selection = event.getSelection();
    treeTopItemSelector.select( selection / increment );
  }
}