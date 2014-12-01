package com.codeaffine.eclipse.ui.progress;

import static com.codeaffine.eclipse.ui.progress.TestItems.cast;

import org.eclipse.jface.viewers.LabelProvider;

class TestItemLabelProvider extends LabelProvider {

  @Override
  public String getText( Object element ) {
    String result = super.getText( element );
    if( element instanceof TestItem ) {
      result = cast( element ).getName();
    }
    return result;
  }
}