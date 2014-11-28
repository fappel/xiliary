package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;

class NativeLayoutFactory<T extends Scrollable> implements LayoutFactory<T> {

  @Override
  public Layout create( Composite parent, T scrollable ) {
    return new FillLayout();
  }
}