package com.codeaffine.eclipse.swt.widget.scrollable;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class ScrollBarConfigurer {

  private final FlatScrollBar scrollBar;

  ScrollBarConfigurer( FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
  }

  void configure( LayoutContext context ) {
    scrollBar.setIncrement( 1 );
    scrollBar.setMaximum( context.getPreferredSize().x );
    scrollBar.setMinimum( context.getOffset() );
    scrollBar.setPageIncrement( context.getVisibleArea().width );
    scrollBar.setThumb( context.getVisibleArea().width );
  }
}