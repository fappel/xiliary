package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;

class PreferredWidthComputer {

  private final Tree tree;
  private final ScrollBar verticalBar;

  PreferredWidthComputer( Tree tree ) {
    this.verticalBar = tree.getVerticalBar();
    this.tree = tree;
  }

  int compute() {
    int result = tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x;
    if( verticalBar.isVisible() ) {
      result = result + verticalBar.getSize().x - BAR_BREADTH;
    }
    return result;
  }
}
