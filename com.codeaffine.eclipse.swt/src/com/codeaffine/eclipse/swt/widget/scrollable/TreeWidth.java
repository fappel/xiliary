package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Tree;

class TreeWidth {

  private final PreferredWidthComputer preferredWidthComputer;
  private final Tree tree;

  private int width;

  TreeWidth( Tree tree ) {
    this( new PreferredWidthComputer( tree ), tree );
  }

  TreeWidth( PreferredWidthComputer expectedWidthComputer, Tree tree ) {
    this.preferredWidthComputer = expectedWidthComputer;
    this.tree = tree;
  }

  void update() {
    width = tree.getSize().x;
  }

  boolean hasScrollEffectingChange() {
    int preferredWidth = preferredWidthComputer.compute();
    boolean result = treeWidthHasChanged( preferredWidth ) && effectsScrollBarSize( preferredWidth );
    return result;
  }

  private boolean effectsScrollBarSize( int preferredWidth ) {
    return   exeedsVisibleRangeWidth( preferredWidth )
          || declinesBackIntoVisibleRangeWidth( preferredWidth );
  }

  private boolean exeedsVisibleRangeWidth( int preferredWidth ) {
    Rectangle parentClientArea = tree.getParent().getClientArea();
    return preferredWidth > parentClientArea.width;
  }

  private boolean declinesBackIntoVisibleRangeWidth( int preferredWidth ) {
    return declines( preferredWidth ) && hasHorizontalScrollBarPadding();
  }

  private boolean treeWidthHasChanged( int preferredWidth ) {
    return preferredWidth != width;
  }

  private boolean declines( int preferredWidth ) {
    return preferredWidth <= width;
  }

  private boolean hasHorizontalScrollBarPadding() {
    Rectangle parentClientArea = tree.getParent().getClientArea();
    return tree.getSize().y < parentClientArea.height;
  }
}