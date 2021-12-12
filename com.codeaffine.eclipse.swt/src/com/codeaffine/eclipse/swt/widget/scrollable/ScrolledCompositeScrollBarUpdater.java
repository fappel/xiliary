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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.ScrollBar;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class ScrolledCompositeScrollBarUpdater implements ScrollBarUpdater {

  private final ScrolledComposite scrolledComposite;
  private final FlatScrollBar scrollBar;
  private final int orientation;

  ScrolledCompositeScrollBarUpdater(
    ScrolledComposite scrolledComposite, FlatScrollBar scrollBar, int orientation )
  {
    this.scrollBar = scrollBar;
    this.scrolledComposite = scrolledComposite;
    this.orientation = orientation;
  }

  @Override
  public void update() {
    Point origin = scrolledComposite.getOrigin();
    ScrollBar scrolledCompositeScrollBar = getScrolledCompositeScrollBar();
    scrollBar.setIncrement( scrolledCompositeScrollBar.getIncrement() );
    scrollBar.setMaximum( scrolledCompositeScrollBar.getMaximum() );
    scrollBar.setMinimum( scrolledCompositeScrollBar.getMinimum() );
    scrollBar.setPageIncrement( scrolledCompositeScrollBar.getPageIncrement() );
    scrollBar.setThumb( scrolledCompositeScrollBar.getThumb() );
    scrollBar.setSelection( getSelection( origin ) );
  }

  private ScrollBar getScrolledCompositeScrollBar() {
    if( orientation == SWT.VERTICAL ) {
      return scrolledComposite.getVerticalBar();
    }
    return scrolledComposite.getHorizontalBar();
  }

  private int getSelection( Point origin ) {
    if( orientation == SWT.VERTICAL ) {
      return origin.y;
    }
    return origin.x;
  }
}