package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Rectangle;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

class TreeWidth {

  private final PreferredWidthComputer preferredWidthComputer;
  private final AdaptionContext<?> context;

  private int width;

  TreeWidth( AdaptionContext<?> context ) {
    this( new PreferredWidthComputer( context ), context );
  }

  TreeWidth( PreferredWidthComputer preferredWidthComputer, AdaptionContext<?> context ) {
    this.preferredWidthComputer = preferredWidthComputer;
    this.context = context;
  }

  void update() {
    width = context.getScrollable().getSize().x;
  }

  boolean hasScrollEffectingChange() {
    int preferredWidth = preferredWidthComputer.compute();
    return treeWidthHasChanged( preferredWidth ) && effectsScrollBarSize( preferredWidth );
  }

  private boolean effectsScrollBarSize( int preferredWidth ) {
    return    exeedsVisibleRangeWidth( preferredWidth )
           || declinesBackIntoVisibleRangeWidth( preferredWidth );
  }

  private boolean exeedsVisibleRangeWidth( int preferredWidth ) {
    Rectangle adapterClientArea = context.getAdapter().getClientArea();
    int visibleAreaWidth = adapterClientArea.width;
    AdaptionContext<?> context = this.context.newContext();
    if( context.isVerticalBarVisible() ) {
      visibleAreaWidth += context.getVerticalBarOffset();
    }
    return preferredWidth > visibleAreaWidth;
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
    Rectangle adapterClientArea = context.getAdapter().getClientArea();
    return context.getScrollable().getSize().y < adapterClientArea.height;
  }
}