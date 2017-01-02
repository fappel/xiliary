/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Rectangle;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

class StructuredScrollableSizeObserver implements SizeObserver {

  private final PreferredWidthComputer preferredWidthComputer;

  private int width;

  StructuredScrollableSizeObserver() {
    this( new PreferredWidthComputer() );
  }

  StructuredScrollableSizeObserver( PreferredWidthComputer preferredWidthComputer ) {
    this.preferredWidthComputer = preferredWidthComputer;
  }

  @Override
  public void update( AdaptionContext<?> context ) {
    width = context.getScrollable().getSize().x;
  }

  @Override
  public boolean mustLayoutAdapter( AdaptionContext<?> context ) {
    int preferredWidth = preferredWidthComputer.compute( context );
    return    isBelowAdapterWidthAndShowsVerticalBar( context )
           || scrollableWidthHasChanged( preferredWidth ) && effectsScrollBarSize( preferredWidth, context );
  }

  private static boolean isBelowAdapterWidthAndShowsVerticalBar( AdaptionContext<?> context ) {
    Rectangle scrollableBounds = context.getScrollable().getBounds();
    Rectangle clientArea = context.getAdapter().getClientArea();
    return    context.getScrollable().isVerticalBarVisible()
           && scrollableBounds.width == clientArea.width;
  }

  private boolean effectsScrollBarSize( int preferredWidth, AdaptionContext<?> context ) {
    return    exeedsVisibleRangeWidth( preferredWidth, context )
           || declinesBackIntoVisibleRangeWidth( preferredWidth, context );
  }

  private static boolean exeedsVisibleRangeWidth( int preferredWidth, AdaptionContext<?> context ) {
    Rectangle adapterClientArea = context.getAdapter().getClientArea();
    int visibleAreaWidth = adapterClientArea.width;
    if( context.isVerticalBarVisible() ) {
      visibleAreaWidth += context.getVerticalBarOffset();
    }
    return preferredWidth > visibleAreaWidth;
  }

  private boolean declinesBackIntoVisibleRangeWidth( int preferredWidth, AdaptionContext<?> context ) {
    return declines( preferredWidth ) && hasHorizontalScrollBarPadding( context );
  }

  private boolean scrollableWidthHasChanged( int preferredWidth ) {
    return preferredWidth != width;
  }

  private boolean declines( int preferredWidth ) {
    return preferredWidth <= width;
  }

  private static boolean hasHorizontalScrollBarPadding( AdaptionContext<?> context ) {
    Rectangle adapterClientArea = context.getAdapter().getClientArea();
    return context.getScrollable().getSize().y < adapterClientArea.height;
  }
}