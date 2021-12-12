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
package com.codeaffine.eclipse.swt.widget.scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

class ScrollableRedrawState {

  private final ScrollableControl<?> scrollable;

  private boolean redrawSuspended;

  ScrollableRedrawState( ScrollableControl<?> scrollable ) {
    this.scrollable = scrollable;
  }

  public void update( boolean redraw ) {
    if( redraw == false ) {
      suspendIfNeeded();
    } else {
      resumeIfNeeded();
    }
  }

  private void suspendIfNeeded() {
    if( !isRedrawSuspended() ) {
      scrollable.setRedraw( false );
      redrawSuspended = true;
    }
  }

  private void resumeIfNeeded() {
    if( isRedrawSuspended() ) {
      scrollable.setRedraw( true );
      redrawSuspended = false;
    }
  }

  private boolean isRedrawSuspended() {
    return redrawSuspended == true;
  }
}