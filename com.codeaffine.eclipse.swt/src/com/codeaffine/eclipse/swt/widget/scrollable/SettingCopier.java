package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.ScrollBar;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class SettingCopier {

  private final ScrollBar from;
  private final FlatScrollBar to;

  SettingCopier( ScrollBar from, FlatScrollBar to ) {
    this.from = from;
    this.to = to;
  }

  void copy() {
    to.setIncrement( from.getIncrement() );
    to.setMaximum( from.getMaximum() );
    to.setMinimum( from.getMinimum() );
    to.setPageIncrement( from.getPageIncrement() );
    to.setSelection( from.getSelection() );
    to.setThumb( from.getThumb() );
  }
}
