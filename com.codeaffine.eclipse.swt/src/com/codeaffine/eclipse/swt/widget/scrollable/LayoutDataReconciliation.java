package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

class LayoutDataReconciliation {

  private final Composite adapter;
  private final Scrollable scrollable;

  private Object data;

  LayoutDataReconciliation( Composite adapter, Scrollable scrollable ) {
    this.adapter = adapter;
    this.scrollable = scrollable;
    this.data = scrollable.getLayoutData();
  }

  void run() {
    if( data != scrollable.getLayoutData() ) {
      data = scrollable.getLayoutData();
      adapter.setLayoutData( data );
      adapter.getParent().layout();
    }
  }
}