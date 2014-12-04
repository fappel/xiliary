package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.VerticalScrollBarUpdater.SELECTION_RASTER_SMOOTH_FACTOR;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TreeVerticalSelectionListener extends SelectionAdapter {

  private final TreeTopItemSelector treeTopItemSelector;

  TreeVerticalSelectionListener( Tree tree ) {
    this.treeTopItemSelector = new TreeTopItemSelector( ( tree ) );
  }

  @Override
  public void widgetSelected( SelectionEvent event ) {
    int selection = ( ( FlatScrollBar )event.widget ).getSelection();
    treeTopItemSelector.select( selection / SELECTION_RASTER_SMOOTH_FACTOR );
  }
}