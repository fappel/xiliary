package com.codeaffine.eclipse.swt.widget.scrollbar;

public class ScrollEvent {

  private final FlatScrollBar scrollBar;
  private final int selection;

  public ScrollEvent( FlatScrollBar scrollBar, int selection ) {
    this.scrollBar = scrollBar;
    this.selection = selection;
  }

  public FlatScrollBar getScrollBar() {
    return scrollBar;
  }

  public int getSelection() {
    return selection;
  }
}