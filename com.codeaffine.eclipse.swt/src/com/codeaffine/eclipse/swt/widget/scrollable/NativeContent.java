package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Layout;

import com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.Content;

class NativeContent implements Content {

  @Override
  public Layout getLayout() {
    return new FillLayout();
  }
}