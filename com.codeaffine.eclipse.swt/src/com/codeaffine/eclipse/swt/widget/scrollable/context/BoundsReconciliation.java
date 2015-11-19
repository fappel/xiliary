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

  BoundsReconciliation( Composite adapter, ScrollableControl<? extends Scrollable> scrollable ) {
    this.adapter = adapter;
    this.scrollable = scrollable;
    registerListeners( scrollable );
    updateBoundsBuffer();
  }

  public void run() {
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

  void treeExpanded() {
    treeEvent = true;
  }

  void treeCollapsed() {
    treeEvent = true;
  }

  private void controlMoved() {
    if( !isSuspended() ) {
      newScrollableBounds = scrollable.getBounds();
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
    boolean result = oldScrollableBounds.equals( newScrollableBounds );
if( !result ) {
  System.out.println(  oldScrollableBounds + "/"  + newScrollableBounds );
}
    return !result;
  }

  private boolean changeByTreeEvent() {
    return treeEvent;
  }

  private void reconcile() {
System.out.println( "reconcile: " + newScrollableBounds );
    adapter.setBounds( newScrollableBounds );
  }

  private void updateBoundsBuffer() {
    oldScrollableBounds = scrollable.getBounds();
    newScrollableBounds = scrollable.getBounds();
  }

  private boolean clearTreeEvent() {
    return treeEvent = false;
  }

  private void registerListeners( ScrollableControl<? extends Scrollable> scrollable ) {
    scrollable.addListener( SWT.RESIZE, evt -> controlResized() );
    scrollable.addListener( SWT.Move, evt -> controlMoved() );
    if( scrollable.isInstanceof( Tree.class ) ) {
      scrollable.addListener( SWT.Collapse, evt -> treeCollapsed() );
      scrollable.addListener( SWT.Expand, evt -> treeExpanded() );
    }
  }
}