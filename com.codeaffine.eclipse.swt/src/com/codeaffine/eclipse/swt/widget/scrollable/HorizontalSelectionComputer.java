package com.codeaffine.eclipse.swt.widget.scrollable;

import static java.lang.Math.max;
import static java.lang.Math.min;

class HorizontalSelectionComputer {

  int compute( LayoutContext<?> context ) {
    return min( selection( context ), availableSpaceForSelection( context ) );
  }

  private static int availableSpaceForSelection( LayoutContext<?> context ) {
    int scrollableWidth = context.getScrollable().getSize().x;
    return max( 0, scrollableWidth - context.getAdapter().getClientArea().width - context.getVerticalBarOffset() );
  }

  private static int selection( LayoutContext<?> context ) {
    if( context.isScrollableReplacedByAdapter() ) {
      return context.getHorizontalAdapterSelection();
    }
    return 0;
  }
}