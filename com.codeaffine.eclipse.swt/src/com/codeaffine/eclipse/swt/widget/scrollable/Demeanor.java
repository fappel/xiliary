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