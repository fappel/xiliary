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
package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.ScrollBar;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class StyledTextHorizontalScrollBarUpdater implements ScrollBarUpdater {

  private final FlatScrollBar scrollBar;
  private final StyledText styledText;

  StyledTextHorizontalScrollBarUpdater( StyledText styledText, FlatScrollBar scrollBar ) {
    this.styledText = styledText;
    this.scrollBar = scrollBar;
  }

  @Override
  public void update() {
    ScrollBar horizontalBar = styledText.getHorizontalBar();
    scrollBar.setIncrement( horizontalBar.getIncrement() );
    scrollBar.setMaximum( horizontalBar.getMaximum() );
    scrollBar.setMinimum( horizontalBar.getMinimum() );
    scrollBar.setPageIncrement( horizontalBar.getPageIncrement() );
    scrollBar.setThumb( horizontalBar.getThumb() );
    scrollBar.setSelection( horizontalBar.getSelection() );
  }
}