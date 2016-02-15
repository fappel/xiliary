package com.codeaffine.eclipse.swt.widget.scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

class PreferredWidthComputer {

  int compute( AdaptionContext<?> context ) {
    int result = context.getPreferredSize().x;
    if( context.isVerticalBarVisible() ) {
      result += context.getVerticalBarOffset();
    }
    return result + context.getOffset() * 2;
  }
}