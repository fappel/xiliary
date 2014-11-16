package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.ScrollBar;

class Visibility {

  private final ScrollBar scrollBar;

  private boolean visibility;

  Visibility( ScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
  }

  boolean hasChanged() {
    return visibility != scrollBar.isVisible();
  }

   void update() {
    visibility = scrollBar.isVisible();
  }

  boolean isVisible() {
    return visibility;
  }
}