package com.codeaffine.eclipse.swt.widget.scrollbar;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

class FlatScrollBarLayout extends Layout {

  private final Orientation orientation;

  public FlatScrollBarLayout( Orientation orientation ) {
    this.orientation = orientation;
  }

  @Override
  protected void layout( Composite composite, boolean flushCache ) {
  }

  @Override
  protected Point computeSize( Composite composite, int wHint, int hHint, boolean flushCache ) {
    return orientation.computeSize( composite, wHint, hHint, flushCache );
  }
}