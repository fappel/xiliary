package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

interface LayoutFactory<T extends Scrollable> {
  Layout create( AdaptionContext<T> context );
  void setIncrementButtonLength( int length );
  int getIncrementButtonLength();
  void setIncrementColor( Color color );
  Color getIncrementColor();
  void setPageIncrementColor( Color color );
  Color getPageIncrementColor();
  void setThumbColor( Color color );
  Color getThumbColor();
  void setBackgroundColor( Color color );
  Color getBackgroundColor();
  ScrollBar getVerticalBarAdapter();
  ScrollBar getHorizontalBarAdapter();
}