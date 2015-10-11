package com.codeaffine.eclipse.swt.widget.scrollable.context;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.ViewForm;
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
    if( adapter.getParent() != null ) {
      if( adapter.getParent().getLayout() instanceof StackLayout ) {
        reconcileStackLayout();
      }
      if( adapter.getParent() instanceof ViewForm ) {
        reconcileViewFormLayout();
      }
    }
  }

  private void reconcileStackLayout() {
    StackLayout stackLayout = ( StackLayout )adapter.getParent().getLayout();
    if( stackLayout.topControl == scrollable ) {
      stackLayout.topControl = adapter;
      adapter.getParent().layout();
    }
  }

  private void reconcileViewFormLayout() {
    ViewForm viewForm = ( ViewForm )adapter.getParent();
    if( viewForm.getContent() == scrollable ) {
      viewForm.setContent( adapter );
      viewForm.layout();
    }
    if( viewForm.getTopCenter() == scrollable ) {
      viewForm.setTopCenter( adapter );
      viewForm.layout();
    }
    if( viewForm.getTopLeft() == scrollable ) {
      viewForm.setTopLeft( adapter );
      viewForm.layout();
    }
    if( viewForm.getTopRight() == scrollable ) {
      viewForm.setTopRight( adapter );
      viewForm.layout();
    }
  }
}
