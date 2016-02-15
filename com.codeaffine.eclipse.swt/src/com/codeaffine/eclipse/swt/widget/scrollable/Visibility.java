package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

class Visibility {

  private final int orientation;

  private boolean visibility;

  Visibility( int orientation ) {
    this.orientation = orientation;
  }

  boolean hasChanged( AdaptionContext<?> context ) {
    return visibility != isScrollBarVisible( context );
  }

  void update( AdaptionContext<?> context ) {
    visibility = isScrollBarVisible( context );
  }

  boolean isVisible() {
    return visibility;
  }

  private boolean isScrollBarVisible( AdaptionContext<?> context ) {
    boolean result = context.isHorizontalBarVisible();
    if( orientation == SWT.VERTICAL ) {
      result = context.isVerticalBarVisible();
    }
    return result;
  }
}