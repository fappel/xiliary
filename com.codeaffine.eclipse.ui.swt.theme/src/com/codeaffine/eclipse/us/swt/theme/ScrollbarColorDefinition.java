package com.codeaffine.eclipse.us.swt.theme;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

class ScrollbarColorDefinition {

  private final Color pageIncrementColor;
  private final Color thumbColor;

  ScrollbarColorDefinition( RGB thumb, RGB pageIncrement ) {
    thumbColor = new Color( Display.getCurrent(), thumb );
    pageIncrementColor = new Color( Display.getCurrent(), pageIncrement );
  }

  Color getPageIncrementColor() {
    return pageIncrementColor;
  }

  Color getThumbColor() {
    return thumbColor;
  }
}