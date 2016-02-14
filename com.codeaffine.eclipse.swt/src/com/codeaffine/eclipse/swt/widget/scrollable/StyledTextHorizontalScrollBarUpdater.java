package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.ScrollBar;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class StyledTextHorizontalScrollBarUpdater implements ScrollBarUpdater {

  private final FlatScrollBar scrollBar;
  private final StyledText styledText;

  StyledTextHorizontalScrollBarUpdater( StyledText styledText, FlatScrollBar scrollBar ) {
    this.styledText = styledText;
    this.scrollBar = scrollBar;
  }

  @Override
  public void update() {
    ScrollBar horizontalBar = styledText.getHorizontalBar();
    scrollBar.setIncrement( horizontalBar.getIncrement() );
    scrollBar.setMaximum( horizontalBar.getMaximum() );
    scrollBar.setMinimum( horizontalBar.getMinimum() );
    scrollBar.setPageIncrement( horizontalBar.getPageIncrement() );
    scrollBar.setThumb( horizontalBar.getThumb() );
    scrollBar.setSelection( horizontalBar.getSelection() );
  }
}