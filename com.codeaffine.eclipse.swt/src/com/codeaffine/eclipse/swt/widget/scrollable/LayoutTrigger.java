package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.Composite;

class LayoutTrigger {

  private final Composite toLayout;

  LayoutTrigger( Composite toLayout ) {
    this.toLayout = toLayout;
  }

  void pull() {
    toLayout.layout();
  }
}