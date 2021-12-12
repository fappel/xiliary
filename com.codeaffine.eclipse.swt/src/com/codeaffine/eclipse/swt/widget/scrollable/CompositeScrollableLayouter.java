/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.BAR_BREADTH;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

class CompositeScrollableLayouter implements ScrollableLayouter {

  private final ScrollableControl<? extends Scrollable> scrollable;

  CompositeScrollableLayouter( ScrollableControl<? extends Scrollable> scrollable ) {
    this.scrollable = scrollable;
  }

  @Override
  public void layout( AdaptionContext<?> context ) {
    scrollable.setSize( computeWidth( context ), computeHeight( context ) );
    scrollable.setLocation( computeLocation( context ) );
  }

  private static Point computeLocation( AdaptionContext<?> context ) {
    int x = context.getOriginOfScrollableOrdinates().x - context.getOffset();
    int y = context.getOriginOfScrollableOrdinates().y - context.getOffset();
    return new Point( x, y );
  }

  private static int computeHeight( AdaptionContext<?> context ) {
    int result = context.getVisibleArea().height;
    if( context.isHorizontalBarVisible() ) {
      result = computeHeightWithHorizontalBarPadding( context );
    }
    return result + context.getOffset() * 2;
  }

  private static int computeHeightWithHorizontalBarPadding( AdaptionContext<?> context ) {
    if( context.getScrollable().getHorizontalBarVisible() ) {
      int horizontalBarHeight = context.getScrollable().getHorizontalBarSize().y;
      return context.getVisibleArea().height - BAR_BREADTH + horizontalBarHeight;
    }
    return context.getVisibleArea().height - BAR_BREADTH;
  }

  private static int computeWidth( AdaptionContext<?> context ) {
    int result = context.getVisibleArea().width;
    if( context.isVerticalBarVisible() ) {
      result = computeWidthWithVerticalBarPadding( context );
    }
    return result + context.getOffset() * 2;
  }

  private static int computeWidthWithVerticalBarPadding( AdaptionContext<?> context ) {
    int verticalBarWidth = context.getScrollable().getVerticalBarSize().x;
    return context.getVisibleArea().width - BAR_BREADTH + verticalBarWidth;
  }
}