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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

class ColorReconciliation {

  private final ScrollableControl<? extends Scrollable> scrollable;
  private final ScrollbarStyle scrollbarStyle;

  ColorReconciliation( ScrollbarStyle scrollbarStyle, ScrollableControl<? extends Scrollable> scrollable ) {
    this.scrollbarStyle = scrollbarStyle;
    this.scrollable = scrollable;
  }

  void run() {
    if( scrollbarStyle != null ) {
      Color scrollableBackground = scrollable.getBackground();
      if( !scrollbarStyle.getBackgroundColor().equals( scrollableBackground ) ) {
        scrollbarStyle.setBackgroundColor( scrollableBackground );
      }
    }
  }
}