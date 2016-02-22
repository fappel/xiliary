/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

class ItemRedrawInsurance {

  void register( ScrollableControl<?> scrollable ) {
    if( mustRedrawOnVerticalScrolling( scrollable ) ) {
      ensureRedrawOnVerticalScrolling( scrollable.getControl() );
    }
  }

  private static boolean mustRedrawOnVerticalScrolling( ScrollableControl<?> scrollable ) {
    return    scrollable.isOwnerDrawn()
           && scrollable.hasStyle( SWT.V_SCROLL )
           && scrollable.hasStyle( SWT.VIRTUAL );
  }

  private static void ensureRedrawOnVerticalScrolling( Scrollable scrollable ) {
    scrollable.getVerticalBar().addListener( SWT.Selection, evt -> scrollable.redraw() );
  }
}