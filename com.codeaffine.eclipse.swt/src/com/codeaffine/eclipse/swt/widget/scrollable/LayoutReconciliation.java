package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

class LayoutReconciliation {

  private final Scrollable scrollable;
  private final Composite adapter;

  LayoutReconciliation( Composite adapter, Scrollable scrollable ) {
    this.adapter = adapter;
    this.scrollable = scrollable;
  }

  void run() {
    if( adapter.getParent().getLayout() instanceof StackLayout ) {
      reconcileStackLayout();
    }
  }

  private void reconcileStackLayout() {
    StackLayout stackLayout = ( StackLayout )adapter.getParent().getLayout();
    if( stackLayout.topControl == scrollable ) {
      stackLayout.topControl = adapter;
      adapter.getParent().layout();
    }
  }
}