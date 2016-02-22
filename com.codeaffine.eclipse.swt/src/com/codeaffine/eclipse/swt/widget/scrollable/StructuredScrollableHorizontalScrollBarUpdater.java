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

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class StructuredScrollableHorizontalScrollBarUpdater implements ScrollBarUpdater {

  private final AdaptionContext<?> context;
  private final FlatScrollBar scrollBar;

  StructuredScrollableHorizontalScrollBarUpdater( AdaptionContext<?> context, FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
    this.context = context;
  }

  @Override
  public void update() {
    AdaptionContext<?> newContext = context.newContext();
    scrollBar.setIncrement( 1 );
    scrollBar.setMaximum( newContext.getPreferredSize().x );
    scrollBar.setMinimum( newContext.getOffset() );
    scrollBar.setPageIncrement( newContext.getVisibleArea().width );
    scrollBar.setThumb( newContext.getVisibleArea().width );
  }
}