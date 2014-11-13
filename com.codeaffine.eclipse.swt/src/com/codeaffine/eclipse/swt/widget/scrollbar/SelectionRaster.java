package com.codeaffine.eclipse.swt.widget.scrollbar;

import static java.lang.Math.abs;

public class SelectionRaster {

  private final FlatScrollBar scrollBar;
  private final int rasterSize;

  public SelectionRaster( FlatScrollBar scrollBar, int rasterSize ) {
    this.scrollBar = scrollBar;
    this.rasterSize = rasterSize;
  }

  public void updateSelection( int selection ) {
    int rasterValue = calculateRasterValue( selection );
    int scrollBarSelection = scrollBar.getSelection();
    if( isDifferentRasterSection( rasterValue, scrollBarSelection ) ) {
      scrollBar.setSelection( rasterValue );
    }
  }

  public int calculateRasterValue( int selection ) {
    return ( selection / rasterSize ) * rasterSize;
  }

  private boolean isDifferentRasterSection( int rasterValue, int scrollBarSelection ) {
    return abs( rasterValue - scrollBarSelection ) >= rasterSize;
  }
}