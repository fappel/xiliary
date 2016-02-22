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

import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.BAR_BREADTH;

public enum Demeanor {

  EXPAND_SCROLL_BAR_ON_MOUSE_OVER {

    @Override
    int getBarBreadth() {
      return BAR_BREADTH;
    }
  },

  FIXED_SCROLL_BAR_BREADTH {

    @Override
    int getBarBreadth() {
      return FIXED_SCROLL_BAR_BREADTH_VALUE;
    }
  };

  private static final int FIXED_SCROLL_BAR_BREADTH_VALUE = BAR_BREADTH * 2;

  abstract int getBarBreadth();
}