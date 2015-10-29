package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static com.codeaffine.eclipse.swt.util.ControlReflectionUtil.$;
import static java.util.stream.Collectors.toList;

import java.util.stream.Stream;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.util.ControlReflectionUtil;

class LayoutReconciliation {

  private final ControlReflectionUtil controlReflectionUtil;
  private final Scrollable scrollable;
  private final Composite adapter;

  LayoutReconciliation( Composite adapter, Scrollable scrollable ) {
    this.controlReflectionUtil = new ControlReflectionUtil();
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
      if( adapter.getParent().getClass().getName().equals( "org.eclipse.ui.part.PageBook" ) ) {
        reconcilePageBookLayout();
      }
      if( adapter.getParent() instanceof SashForm ) {
        reconcileSashLayout();
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

  private void reconcilePageBookLayout() {
    Composite parent = adapter.getParent();
    if( controlReflectionUtil.getField( parent, "currentPage", Control.class ) == scrollable ) {
      controlReflectionUtil.invoke( parent, "showPage", $( adapter, Control.class ) );
      adapter.getParent().layout();
    }
  }

  private void reconcileSashLayout() {
    SashForm parent = ( SashForm )adapter.getParent();
    if( needsReparenting( parent ) ) {
      scrollable.setParent( adapter );
      controlReflectionUtil.setField( scrollable, "parent", adapter.getParent() );
      adapter.getParent().layout();
    }
    if( controlReflectionUtil.getField( parent, "maxControl", Control.class ) == scrollable ) {
      controlReflectionUtil.setField( parent, "maxControl", adapter );
      adapter.getParent().layout();
    }
  }

  private boolean needsReparenting( Composite parent ) {
    return !Stream.of( parent.getChildren() )
      .filter( control -> control == scrollable )
      .collect( toList() )
      .isEmpty();
  }
}