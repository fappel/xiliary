package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Scrollable;

class TreeWidth {

  private final PreferredWidthComputer preferredWidthComputer;
  private final LayoutContextFactory contextFactory;
  private final Scrollable scrollable;

  private int width;

  TreeWidth( Scrollable scrollable, LayoutContextFactory contextFactory ) {
    this( new PreferredWidthComputer( scrollable, contextFactory ), scrollable, contextFactory );
  }

  TreeWidth( PreferredWidthComputer widthComputer, Scrollable scrollable, LayoutContextFactory contextFactory ) {
    this.preferredWidthComputer = widthComputer;
    this.contextFactory = contextFactory;
    this.scrollable = scrollable;
  }

  void update() {
    width = scrollable.getSize().x;
  }

  boolean hasScrollEffectingChange() {
    int preferredWidth = preferredWidthComputer.compute();
    return treeWidthHasChanged( preferredWidth ) && effectsScrollBarSize( preferredWidth );
  }

  private boolean effectsScrollBarSize( int preferredWidth ) {
    return   exeedsVisibleRangeWidth( preferredWidth )
          || declinesBackIntoVisibleRangeWidth( preferredWidth );
  }

  private boolean exeedsVisibleRangeWidth( int preferredWidth ) {
    Rectangle parentClientArea = scrollable.getParent().getClientArea();
    int visibleAreaWidth = parentClientArea.width;
    LayoutContext context = contextFactory.create();
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
    Rectangle parentClientArea = scrollable.getParent().getClientArea();
    return scrollable.getSize().y < parentClientArea.height;
  }
}