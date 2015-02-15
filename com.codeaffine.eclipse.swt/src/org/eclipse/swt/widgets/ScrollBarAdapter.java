package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.Point;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class ScrollBarAdapter extends ScrollBar {

  private FlatScrollBar scrollBar;

  private ScrollBarAdapter() {
    super( null, -1 );
  }

  public void setScrollbar( FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
  }

  @Override
  public Point getSize() {
    return scrollBar.getSize();
  }
}