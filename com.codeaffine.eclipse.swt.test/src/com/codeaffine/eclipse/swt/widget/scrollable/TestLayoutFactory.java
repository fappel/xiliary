package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;

class TestLayoutFactory implements LayoutFactory<Scrollable> {

  private final Layout expected;

  TestLayoutFactory( Layout expected ) {
    this.expected = expected;
  }

  @Override
  public Layout create( LayoutContext<Scrollable> context ) {
    return expected;
  }

  @Override
  public void setIncrementButtonLength( int length ) {
  }

  @Override
  public int getIncrementButtonLength() {
    return 0;
  }

  @Override
  public void setIncrementColor( Color color ) {
  }

  @Override
  public Color getIncrementColor() {
    return null;
  }

  @Override
  public void setPageIncrementColor( Color color ) {
  }

  @Override
  public Color getPageIncrementColor() {
    return null;
  }

  @Override
  public void setThumbColor( Color color ) {
  }

  @Override
  public Color getThumbColor() {
    return null;
  }

  @Override
  public ScrollBar getVerticalBarAdapter() {
    return null;
  }

  @Override
  public ScrollBar getHorizontalBarAdapter() {
    return null;
  }
}