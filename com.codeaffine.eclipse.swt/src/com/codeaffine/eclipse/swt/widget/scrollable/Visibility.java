package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ScrollBar;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

class Visibility {

  private final AdaptionContext<?> context;
  private final ScrollBar scrollBar;

  private boolean visibility;

  Visibility( ScrollBar scrollBar, AdaptionContext<?> context ) {
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
    AdaptionContext<?> context = this.context.newContext();
    boolean result = context.isHorizontalBarVisible();
    if( ( scrollBar.getStyle() & SWT.VERTICAL ) > 0 ) {
      result = context.isVerticalBarVisible();
    }
    return result;
  }
}