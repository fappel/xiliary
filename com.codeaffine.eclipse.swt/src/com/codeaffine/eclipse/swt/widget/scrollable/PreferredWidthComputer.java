package com.codeaffine.eclipse.swt.widget.scrollable;


class PreferredWidthComputer {

  private final LayoutContextFactory contextFactory;

  PreferredWidthComputer( LayoutContextFactory contextFactory ) {
    this.contextFactory = contextFactory;
  }

  int compute() {
    LayoutContext context = contextFactory.create();
    int result = context.getPreferredSize().x;
    if( context.isVerticalBarVisible() ) {
      result += context.getVerticalBarOffset();
    }
    return result + context.getOffset() * 2;
  }
}