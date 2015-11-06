package com.codeaffine.eclipse.swt.widget.scrollable.context;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

class EnablementReconciliation {

  private final ScrollableControl<? extends Scrollable> scrollable;
  private final Composite adapter;

  boolean scrollableEnabled;

  EnablementReconciliation( Composite adapter, ScrollableControl<? extends Scrollable> scrollable ) {
    this.adapter = adapter;
    this.scrollable = scrollable;
    this.scrollableEnabled = scrollable.getEnabled();
  }

  void run() {
    scrollableEnabled = scrollable.getEnabled();
    if( adapter.getEnabled() != scrollableEnabled ) {
      adapter.setEnabled( scrollableEnabled );
    }
  }

  boolean setEnabled( boolean enabled ) {
    boolean result = enabled;
    if( enableAdapter( enabled ) && enableScrollable() ) {
      result = true;
    } else if( enableAdapter( enabled ) && disableScrollable() ) {
      result = false;
    } else if( disableAdapter( enabled ) && enableScrollable() ) {
      result = true;
    } else if( disableAdapter( enabled ) && disableScrollable() ) {
      result = false;
    }
    scrollable.setEnabled( result );
    scrollableEnabled = scrollable.getEnabled();
    return result;
  }

  private boolean enableAdapter( boolean enabled ) {
    return !adapter.getEnabled() && enabled;
  }

  private boolean disableAdapter( boolean enabled ) {
    return adapter.getEnabled() && !enabled;
  }

  private boolean enableScrollable() {
    return !scrollableEnabled && scrollable.getEnabled() ;
  }

  private boolean disableScrollable() {
    return scrollableEnabled && !scrollable.getEnabled() ;
  }
}