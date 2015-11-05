package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

class Visibility {

  private final AdaptionContext<?> context;
  private final int orientation;

  private boolean visibility;

  Visibility( int orientation, AdaptionContext<?> context ) {
    this.orientation = orientation;
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
    if( orientation == SWT.VERTICAL ) {
      result = context.isVerticalBarVisible();
    }
    return result;
  }
}