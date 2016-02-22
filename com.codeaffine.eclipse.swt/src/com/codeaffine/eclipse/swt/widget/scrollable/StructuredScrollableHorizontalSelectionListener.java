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

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class StructuredScrollableHorizontalSelectionListener extends SelectionAdapter {

  private final AdaptionContext<?> context;

  StructuredScrollableHorizontalSelectionListener( AdaptionContext<?> context ) {
    this.context = context;
  }

  @Override
  public void widgetSelected( SelectionEvent event ) {
    updateLocation( new Point( -getSelection( event ), computeHeight() ) );
  }

  private int computeHeight() {
    if( context.isScrollableReplacedByAdapter() ) {
      return -context.getBorderWidth();
    }
    return context.getScrollable().getLocation().y - context.getBorderWidth();
  }

  private int getSelection( SelectionEvent event ) {
    return ( ( FlatScrollBar )event.widget ).getSelection() + context.getBorderWidth();
  }

  private void updateLocation( final Point result ) {
    context.getReconciliation().runWhileSuspended( () -> context.getScrollable().setLocation( result ) );
  }
}