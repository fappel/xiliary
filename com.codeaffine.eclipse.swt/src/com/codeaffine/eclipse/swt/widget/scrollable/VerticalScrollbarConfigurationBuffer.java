/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
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