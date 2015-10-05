package com.codeaffine.eclipse.swt.widget.scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

class PreferredWidthComputer {

  private final AdaptionContext<?> context;

  PreferredWidthComputer( AdaptionContext<?> context ) {
    this.context = context;
  }

  int compute() {
    AdaptionContext<?> context = this.context.newContext();
    int result = context.getPreferredSize().x;
    if( context.isVerticalBarVisible() ) {
      result += context.getVerticalBarOffset();
    }
    return result + context.getOffset() * 2;
  }
}