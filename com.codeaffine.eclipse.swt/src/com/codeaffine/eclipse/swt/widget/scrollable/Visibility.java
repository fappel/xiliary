package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ScrollBar;

class Visibility {

  private final LayoutContext<?> context;
  private final ScrollBar scrollBar;

  private boolean visibility;

  Visibility( ScrollBar scrollBar, LayoutContext<?> context ) {
    this.scrollBar = scrollBar;
    this.context = context;
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
    LayoutContext<?> context = this.context.newContext();
    boolean result = context.isHorizontalBarVisible();
    if( ( scrollBar.getStyle() & SWT.VERTICAL ) > 0 ) {
      result = context.isVerticalBarVisible();
    }
    return result;
  }
}