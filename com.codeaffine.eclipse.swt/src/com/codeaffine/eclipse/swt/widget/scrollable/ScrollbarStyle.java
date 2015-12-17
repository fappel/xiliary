package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Color;

public interface ScrollbarStyle {
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
  void setDemeanor( Demeanor demeanor );
  Demeanor getDemeanor();
}