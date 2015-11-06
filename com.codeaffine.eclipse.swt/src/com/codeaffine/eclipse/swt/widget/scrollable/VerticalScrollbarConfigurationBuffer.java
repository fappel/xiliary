package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

class VerticalScrollbarConfigurationBuffer {

  private static final int INITIALIZATION_TRIGGER = -1;

  private final ScrollableControl<? extends Scrollable> scrollable;

  private int pageIncrement;
  private int selection;
  private int increment;
  private int maximum;
  private int thumb;

  VerticalScrollbarConfigurationBuffer( ScrollableControl<? extends Scrollable> scrollable ) {
    this.scrollable = scrollable;
    this.maximum = INITIALIZATION_TRIGGER;
  }

  void update() {
    increment = scrollable.getVerticalBarIncrement();
    maximum = scrollable.getVerticalBarMaximum();
    pageIncrement = scrollable.getVerticalBarPageIncrement();
    thumb = scrollable.getVerticalBarThumb();
    selection = scrollable.getVerticalBarSelection();
  }

  boolean hasChanged() {
    return    increment != scrollable.getVerticalBarIncrement()
           || maximum != scrollable.getVerticalBarMaximum()
           || pageIncrement != scrollable.getVerticalBarPageIncrement()
           || thumb != scrollable.getVerticalBarThumb()
           || selection != scrollable.getVerticalBarSelection();
  }
}