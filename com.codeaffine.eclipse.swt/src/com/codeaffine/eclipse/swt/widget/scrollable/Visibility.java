package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ScrollBar;

class Visibility {

  private final LayoutContextFactory contextFactory;
  private final ScrollBar scrollBar;

  private boolean visibility;

  Visibility( ScrollBar scrollBar, LayoutContextFactory contextFactory ) {
    this.scrollBar = scrollBar;
    this.contextFactory = contextFactory;
  }

  boolean hasChanged() {
    return visibility != isScrollBarVisible();
  }

   void update() {
    visibility = isScrollBarVisible();
  }

  boolean isVisible() {
    return visibility;
  }

  private boolean isScrollBarVisible() {
    LayoutContext context = contextFactory.create();
    boolean result = context.isHorizontalBarVisible();
    if( ( scrollBar.getStyle() & SWT.VERTICAL ) > 0 ) {
      result = context.isVerticalBarVisible();
    }
    return result;
  }
}