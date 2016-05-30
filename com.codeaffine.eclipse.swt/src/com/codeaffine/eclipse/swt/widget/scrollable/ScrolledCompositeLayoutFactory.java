/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Layout;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class ScrolledCompositeLayoutFactory extends ScrollableLayoutFactory<ScrolledComposite> {

  @Override
  public Layout create( AdaptionContext<ScrolledComposite> context, FlatScrollBar horizontal, FlatScrollBar vertical ) {
    ScrollableLayouter layouter = new CompositeScrollableLayouter( context.getScrollable() );
    return new ScrollableLayout( newContext( context ), layouter, horizontal, vertical, getCornerOverlay() );
  }

  @Override
  public SelectionListener createHorizontalSelectionListener( AdaptionContext<ScrolledComposite> context ) {
    return new ScrolledCompositeSelectionListener( context.getScrollable().getControl(), SWT.HORIZONTAL );
  }

  @Override
  public SelectionListener createVerticalSelectionListener( AdaptionContext<ScrolledComposite> context ) {
    return new ScrolledCompositeSelectionListener( context.getScrollable().getControl(), SWT.VERTICAL );
  }

  @Override
  public DisposeListener createWatchDog(
    AdaptionContext<ScrolledComposite> context, FlatScrollBar horizontal, FlatScrollBar vertical )
  {
    ScrolledComposite control = context.getScrollable().getControl();
    ScrollBarUpdater horizontalUpdater = new ScrolledCompositeScrollBarUpdater( control, horizontal, SWT.HORIZONTAL );
    ScrollBarUpdater verticalUpdater = new ScrolledCompositeScrollBarUpdater( control, vertical, SWT.VERTICAL );
    SizeObserver sizeObserver = new CompositeSizeObserver( control );
    return new WatchDog( newContext( context ), horizontalUpdater, verticalUpdater, sizeObserver );
  }

  private static AdaptionContext<ScrolledComposite> newContext( AdaptionContext<ScrolledComposite> context ) {
    return context.newContext( context.getScrollable().getItemHeight() );
  }
}