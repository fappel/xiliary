package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;

class PreferredWidthComputer {

  private final Tree tree;

  PreferredWidthComputer( Tree tree ) {
    this.tree = tree;
  }

  int compute() {
    int result = tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x;
    TreeLayoutContext context = new TreeLayoutContext( tree );
    if( context.isVerticalBarVisible() ) {
      result += context.getVerticalBarOffset();
    }
    return result;
  }
}