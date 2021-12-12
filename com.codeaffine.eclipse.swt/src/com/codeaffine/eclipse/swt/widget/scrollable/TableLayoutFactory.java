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

import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Table;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TableLayoutFactory extends ScrollableLayoutFactory<Table> {

  @Override
  public Layout create( AdaptionContext<Table> context, FlatScrollBar horizontal, FlatScrollBar vertical ) {
    ScrollableLayouter layouter = new StructuredScrollableLayouter( context.getScrollable() );
    return new ScrollableLayout( newContext( context ), layouter, horizontal, vertical, getCornerOverlay() );
  }

  @Override
  public SelectionListener createHorizontalSelectionListener( AdaptionContext<Table> context ) {
    return new StructuredScrollableHorizontalSelectionListener( context );
  }

  @Override
  public SelectionListener createVerticalSelectionListener( AdaptionContext<Table> context ) {
    return new TableVerticalSelectionListener( context.getScrollable().getControl() );
  }

  @Override
  public DisposeListener createWatchDog(
    AdaptionContext<Table> context, FlatScrollBar horizontal, FlatScrollBar vertical )
  {
    Table control = context.getScrollable().getControl();
    ScrollBarUpdater horizontalUpdater = new StructuredScrollableHorizontalScrollBarUpdater( context, horizontal );
    ScrollBarUpdater verticalUpdater = new TableVerticalScrollBarUpdater( control, vertical );
    SizeObserver sizeObserver = new StructuredScrollableSizeObserver();
    return new WatchDog( newContext( context ), horizontalUpdater, verticalUpdater, sizeObserver );
  }

  private static AdaptionContext<Table> newContext( AdaptionContext<Table> context ) {
    return context.newContext( context.getScrollable().getItemHeight() );
  }
}