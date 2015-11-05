package com.codeaffine.eclipse.swt.widget.scrollable.context;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

class ColorReconciliation {

  private final ScrollableControl<? extends Scrollable> scrollable;
  private final ScrollbarStyle scrollbarStyle;

  ColorReconciliation( ScrollbarStyle scrollbarStyle, ScrollableControl<? extends Scrollable> scrollable ) {
    this.scrollbarStyle = scrollbarStyle;
    this.scrollable = scrollable;
  }

  void run() {
    if( scrollbarStyle != null ) {
      Color scrollableBackground = scrollable.getBackground();
      if( !scrollbarStyle.getBackgroundColor().equals( scrollableBackground ) ) {
        scrollbarStyle.setBackgroundColor( scrollableBackground );
      }
    }
  }
}