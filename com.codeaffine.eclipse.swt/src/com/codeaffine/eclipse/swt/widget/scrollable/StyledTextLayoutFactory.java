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

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Layout;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class StyledTextLayoutFactory extends ScrollableLayoutFactory<StyledText> {

  @Override
  public Layout create( AdaptionContext<StyledText> context, FlatScrollBar horizontal, FlatScrollBar vertical ) {
    ScrollableLayouter layouter = new CompositeScrollableLayouter( context.getScrollable() );
    return new ScrollableLayout( newContext( context ), layouter, horizontal, vertical, getCornerOverlay() );
  }

  @Override
  public SelectionListener createHorizontalSelectionListener( AdaptionContext<StyledText> context ) {
    return new StyledTextHorizontalSelectionListener( context.getScrollable().getControl() );
  }

  @Override
  public SelectionListener createVerticalSelectionListener( AdaptionContext<StyledText> context ) {
    return new StyledTextVerticalSelectionListener( context.getScrollable().getControl() );
  }

  @Override
  public DisposeListener createWatchDog(
    AdaptionContext<StyledText> context, FlatScrollBar horizontal, FlatScrollBar vertical )
  {
    StyledText control = context.getScrollable().getControl();
    ScrollBarUpdater horizontalUpdater = new StyledTextHorizontalScrollBarUpdater( control, horizontal );
    ScrollBarUpdater verticalUpdater = new StyledTextVerticalScrollBarUpdater( control, vertical );
    SizeObserver sizeObserver = new CompositeSizeObserver( control );
    return new WatchDog( newContext( context ), horizontalUpdater, verticalUpdater, sizeObserver );
  }

  private static AdaptionContext<StyledText> newContext( AdaptionContext<StyledText> context ) {
    return context.newContext( context.getScrollable().getItemHeight() );
  }
}