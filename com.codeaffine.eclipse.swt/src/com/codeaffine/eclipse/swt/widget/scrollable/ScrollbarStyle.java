package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Color;

interface ScrollbarStyle {
  void setIncrementButtonLength( int length );
  int getIncrementButtonLength();
  void setIncrementColor( Color color );
  Color getIncrementColor();
  void setPageIncrementColor( Color color );
  Color getPageIncrementColor();
  void setThumbColor( Color color );
  Color getThumbColor();
}