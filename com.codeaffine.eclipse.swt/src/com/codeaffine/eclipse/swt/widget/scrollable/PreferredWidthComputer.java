package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Scrollable;

class PreferredWidthComputer {

  private final LayoutContextFactory contextFactory;
  private final Scrollable scrollable;

  PreferredWidthComputer( Scrollable scrollable, LayoutContextFactory contextFactory ) {
    this.scrollable = scrollable;
    this.contextFactory = contextFactory;
  }

  int compute() {
    int result = scrollable.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x;
    LayoutContext context = contextFactory.create();
    if( context.isVerticalBarVisible() ) {
      result += context.getVerticalBarOffset();
    }
    return result;
  }
}