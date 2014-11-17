package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;

class Visibility {

  private final ScrollBar scrollBar;

  private boolean visibility;

  Visibility( ScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
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
    TreeLayoutContext context = new TreeLayoutContext( ( Tree )scrollBar.getParent() );
    boolean result = context.isHorizontalBarVisible();
    if( ( scrollBar.getStyle() & SWT.VERTICAL ) > 0 ) {
      result = context.isVerticalBarVisible();
    }
    return result;
  }
}