package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class ScrollBarFactory {

  <T extends Scrollable> FlatScrollBar create( Composite parent, T scrollable, int direction  ) {
    FlatScrollBar result = new FlatScrollBar( parent, direction );
    result.setBackground( scrollable.getBackground() );
    return result;
  }
}