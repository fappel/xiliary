/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable.context;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

class EnablementReconciliation {

  private final ScrollableControl<? extends Scrollable> scrollable;
  private final Composite adapter;

  boolean scrollableEnabled;
  boolean adapterEnabled;

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
    if( scrollableEnabled && adapterEnabled != adapter.isEnabled() ) {
      adapter.setEnabled( scrollableEnabled );
      adapterEnabled = adapter.isEnabled();
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
    adapterEnabled = adapter.isEnabled();
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