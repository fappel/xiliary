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
package com.codeaffine.eclipse.swt.widget.scrollable.context;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Tree;

class BoundsReconciliation {

  private final ScrollableControl<? extends Scrollable> scrollable;
  private final Composite adapter;

  private Rectangle oldScrollableBounds;
  private Rectangle newScrollableBounds;
  private boolean treeEvent;
  private int suspendCount;
  private boolean moved;

  BoundsReconciliation( Composite adapter, ScrollableControl<? extends Scrollable> scrollable ) {
    this.adapter = adapter;
    this.scrollable = scrollable;
    registerListeners( scrollable );
    updateBoundsBuffer();
  }

  void run() {
    if( mustReconcile() ) {
      reconcile();
    }
    updateBoundsBuffer();
    clearTreeEvent();
  }

  boolean isSuspended() {
    return suspendCount > 0;
  }

  void suspend() {
    suspendCount++;
  }

  void resume() {
    suspendCount--;
  }

  void runSuspended( Runnable runnable ) {
    suspend();
    try {
      runnable.run();
    } finally {
      updateBoundsBuffer();
      resume();
    }
  }

  private void treeExpanded() {
    treeEvent = true;
  }

  private void treeCollapsed() {
    treeEvent = true;
  }

  private void controlMoved() {
    if( !isSuspended() ) {
      newScrollableBounds = scrollable.getBounds();
      flagScrollableAsMoved();
      if( mustWorkaroundScrollableWithBorderInitializations() ) {
        oldScrollableBounds = scrollable.getBounds();
      }
    }
  }

  private boolean mustWorkaroundScrollableWithBorderInitializations() {
    return    newScrollableBounds.x == newScrollableBounds.y
           && newScrollableBounds.x == -scrollable.getBorderWidth()
           && scrollable.hasStyle( SWT.BORDER );
  }

  private void controlResized() {
    if( !isSuspended() ) {
      newScrollableBounds = scrollable.getBounds();
    }
  }

  private boolean mustReconcile() {
    return scrollableBoundsHaveBeenChanged() && !changeByTreeEvent();
  }

  private boolean scrollableBoundsHaveBeenChanged() {
    return !oldScrollableBounds.equals( newScrollableBounds );
  }

  private boolean changeByTreeEvent() {
    return treeEvent;
  }

  private void reconcile() {
    if( hasScrollableBeenMoved() ) {
      unflagScrollableAsMoved();
      newScrollableBounds = computeScrollableBoundsWithLocationDelta();
    }
    adapter.setBounds( newScrollableBounds );
  }

  private Rectangle computeScrollableBoundsWithLocationDelta() {
    return new Rectangle( newScrollableBounds.x - oldScrollableBounds.x,
                          newScrollableBounds.y - oldScrollableBounds.y,
                          newScrollableBounds.width,
                          newScrollableBounds.height );
  }

  private void updateBoundsBuffer() {
    oldScrollableBounds = scrollable.getBounds();
    newScrollableBounds = scrollable.getBounds();
  }

  private boolean clearTreeEvent() {
    return treeEvent = false;
  }

  private void registerListeners( ScrollableControl<? extends Scrollable> scrollable ) {
    scrollable.addListener( SWT.Move, evt -> controlMoved() );
    scrollable.addListener( SWT.Resize, evt -> controlResized() );
    if( scrollable.isInstanceof( Tree.class ) ) {
      scrollable.addListener( SWT.Collapse, evt -> treeCollapsed() );
      scrollable.addListener( SWT.Expand, evt -> treeExpanded() );
    }
  }

  private void flagScrollableAsMoved() {
    moved = true;
  }

  private void unflagScrollableAsMoved() {
    moved = false;
  }

  private boolean hasScrollableBeenMoved() {
    return moved;
  }
}