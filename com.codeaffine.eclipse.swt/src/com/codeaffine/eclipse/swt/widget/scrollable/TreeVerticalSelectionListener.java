package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.VerticalScrollBarUpdater.SELECTION_RASTER_SMOOTH_FACTOR;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TreeVerticalSelectionListener extends SelectionAdapter {

  private final TreeTopItemSelector treeTopItemSelector;

  TreeVerticalSelectionListener( AdaptionContext<Tree> context ) {
    this.treeTopItemSelector = new TreeTopItemSelector( ( context.getScrollable().getControl() ) );
  }

  @Override
  public void widgetSelected( SelectionEvent event ) {
    int selection = ( ( FlatScrollBar )event.widget ).getSelection();
    treeTopItemSelector.select( selection / SELECTION_RASTER_SMOOTH_FACTOR );
  }
}