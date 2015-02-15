package com.codeaffine.eclipse.swt.widget.scrollable;

class PreferredWidthComputer {

  private final LayoutContext<?> context;

  PreferredWidthComputer( LayoutContext<?> context ) {
    this.context = context;
  }

  int compute() {
    LayoutContext<?> context = this.context.newContext();
    int result = context.getPreferredSize().x;
    if( context.isVerticalBarVisible() ) {
      result += context.getVerticalBarOffset();
    }
    return result + context.getOffset() * 2;
  }
}