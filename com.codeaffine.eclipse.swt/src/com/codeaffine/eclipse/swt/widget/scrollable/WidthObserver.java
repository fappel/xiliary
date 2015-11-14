package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Rectangle;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

class WidthObserver {

  private final PreferredWidthComputer preferredWidthComputer;
  private final AdaptionContext<?> context;

  private int width;

  WidthObserver( AdaptionContext<?> context ) {
    this( new PreferredWidthComputer( context ), context );
  }

  WidthObserver( PreferredWidthComputer preferredWidthComputer, AdaptionContext<?> context ) {
    this.preferredWidthComputer = preferredWidthComputer;
    this.context = context;
  }

  void update() {
    width = context.getScrollable().getSize().x;
  }

  boolean hasScrollEffectingChange() {
    int preferredWidth = preferredWidthComputer.compute();
    return    isBelowAdapterWidthAndShowsVerticalBar()
           || scrollableWidthHasChanged( preferredWidth ) && effectsScrollBarSize( preferredWidth );
  }

  private boolean isBelowAdapterWidthAndShowsVerticalBar() {
    Rectangle scrollableBounds = context.getScrollable().getBounds();
    Rectangle clientArea = context.getAdapter().getClientArea();
    return    context.getScrollable().isVerticalBarVisible()
           && scrollableBounds.width == clientArea.width;
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

  private boolean scrollableWidthHasChanged( int preferredWidth ) {
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