package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;

class NativeLayoutFactory<T extends Scrollable> implements LayoutFactory<T> {

  static final Class<FillLayout> LAYOUT_TYPE = FillLayout.class;

  @Override
  public Layout create( LayoutContext<T> context ) {
    try {
      return LAYOUT_TYPE.newInstance();
    } catch( Exception shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
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