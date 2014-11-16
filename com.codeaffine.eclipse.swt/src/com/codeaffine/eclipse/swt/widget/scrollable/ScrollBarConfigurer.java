package com.codeaffine.eclipse.swt.widget.scrollable;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class ScrollBarConfigurer {

  private final FlatScrollBar scrollBar;

  ScrollBarConfigurer( FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
  }

  void configure( TreeLayoutContext context ) {
    scrollBar.setIncrement( 1 );
    scrollBar.setMaximum( context.getPreferredSize().x );
    scrollBar.setMinimum( 0 );
    scrollBar.setPageIncrement( context.getVisibleArea().width );
    scrollBar.setThumb( context.getVisibleArea().width );
  }
}