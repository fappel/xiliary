/**
 * Copyright (c) 2014 - 2017 Frank Appel
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

class VisibilityReconciliation {

  private final ScrollableControl<? extends Scrollable> scrollable;
  private final Composite adapter;

  boolean scrollableVisible;

  VisibilityReconciliation( Composite adapter, ScrollableControl<? extends Scrollable> scrollable ) {
    this.adapter = adapter;
    this.scrollable = scrollable;
    this.scrollableVisible = scrollable.getVisible();
  }

  void run() {
    scrollableVisible = scrollable.getVisible();
    if( adapter.getVisible() != scrollableVisible ) {
      adapter.setVisible( scrollableVisible );
    }
  }

  boolean setVisible( boolean visible ) {
    boolean result = visible;
    if( showAdapter( visible ) && showScrollable() ) {
      result = true;
    } else if( showAdapter( visible ) && hideScrollable() ) {
      result = false;
    } else if( hideAdapter( visible ) && showScrollable() ) {
      result = true;
    } else if( hideAdapter( visible ) && hideScrollable() ) {
      result = false;
    }
    scrollable.setVisible( result );
    scrollableVisible = scrollable.getVisible();
    return result;
  }

  private boolean showAdapter( boolean visible ) {
    return !adapter.getVisible() && visible;
  }

  private boolean hideAdapter( boolean visible ) {
    return adapter.getVisible() && !visible;
  }

  private boolean showScrollable() {
    return !scrollableVisible && scrollable.getVisible() ;
  }

  private boolean hideScrollable() {
    return scrollableVisible && !scrollable.getVisible() ;
  }
}