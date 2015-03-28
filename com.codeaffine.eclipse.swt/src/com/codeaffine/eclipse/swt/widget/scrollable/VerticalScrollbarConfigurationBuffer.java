package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;

class VerticalScrollbarConfigurationBuffer {

  private static final int INITIALIZATION_TRIGGER = -1;

  private final Scrollable scrollable;

  private int increment;
  private int maximum;
  private int pageIncrement;
  private int thumb;
  private int selection;

  VerticalScrollbarConfigurationBuffer( Scrollable scrollable ) {
    this.scrollable = scrollable;
    maximum = INITIALIZATION_TRIGGER;

  }

  boolean hasChanged() {
    if( hasVerticalBar() ) {
      return    increment != getVerticalBar().getIncrement()
             || maximum != getVerticalBar().getMaximum()
             || pageIncrement != getVerticalBar().getPageIncrement()
             || thumb != getVerticalBar().getThumb()
             || selection != getVerticalBar().getSelection();
    }
    return true;
  }

  void update() {
    if( hasVerticalBar() ) {
      increment = getVerticalBar().getIncrement();
      maximum = getVerticalBar().getMaximum();
      pageIncrement = getVerticalBar().getPageIncrement();
      thumb = getVerticalBar().getThumb();
      selection = getVerticalBar().getSelection();
    }
  }

  private boolean hasVerticalBar() {
    return getVerticalBar() != null;
  }

  private ScrollBar getVerticalBar() {
    return scrollable.getVerticalBar();
  }
}