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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.Reconciliation;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class ScrollableLayout extends Layout {

  private final ScrollableLayouter scrollableLayouter;
  private final OverlayLayouter overlayLayouter;
  private final Reconciliation reconciliation;
  private final AdaptionContext<?> context;

  ScrollableLayout( AdaptionContext<?> context,
                    ScrollableLayouter scrollableLayouter,
                    FlatScrollBar horizontal,
                    FlatScrollBar vertical,
                    Label cornerOverlay )
  {
    this( context,
          new OverlayLayouter( horizontal, vertical, cornerOverlay ),
          scrollableLayouter,
          context.getReconciliation() );
  }

  ScrollableLayout( AdaptionContext<?> context,
                    OverlayLayouter overlayLayouter,
                    ScrollableLayouter scrollableLayouter,
                    Reconciliation reconciliation  )
  {
    this.context = context;
    this.overlayLayouter = overlayLayouter;
    this.scrollableLayouter = scrollableLayouter;
    this.reconciliation = reconciliation;
  }

  @Override
  protected void layout( Composite composite, boolean flushCache ) {
    reconciliation.runWhileSuspended( () -> layout() );
  }

  @Override
  protected Point computeSize( Composite composite, int wHint, int hHint, boolean flushCache ) {
    return context.getScrollable().computeSize( wHint, hHint, flushCache );
  }

  private void layout() {
    scrollableLayouter.layout( context.newContext() );
    overlayLayouter.layout( context.newContext() );
  }
}