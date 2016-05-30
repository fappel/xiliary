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
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class ScrolledCompositeSelectionListener extends SelectionAdapter {

  private final ScrolledComposite scrolledComposite;
  private final int orientation;

  ScrolledCompositeSelectionListener( ScrolledComposite scrolledComposite, int orientation ) {
    this.scrolledComposite = scrolledComposite;
    this.orientation = orientation;
  }

  @Override
  public void widgetSelected( SelectionEvent event ) {
    Point origin = scrolledComposite.getOrigin();
    int selection = ( ( FlatScrollBar )event.widget ).getSelection();
    if( orientation == SWT.VERTICAL ) {
      scrolledComposite.setOrigin( origin.x, selection );
    } else {
      scrolledComposite.setOrigin( selection, origin.y );
    }
  }
}