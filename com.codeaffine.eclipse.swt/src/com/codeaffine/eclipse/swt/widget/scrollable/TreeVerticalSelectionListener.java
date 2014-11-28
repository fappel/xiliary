package com.codeaffine.eclipse.swt.widget.scrollable;

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
    treeTopItemSelector.select( ( ( FlatScrollBar )event.widget ).getSelection() );
  }
}