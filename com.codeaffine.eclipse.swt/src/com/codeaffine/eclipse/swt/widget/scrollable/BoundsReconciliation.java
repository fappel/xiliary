package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Tree;

class BoundsReconciliation implements ControlListener, TreeListener {

  private final Composite adapter;
  private final Scrollable scrollable;

  private Rectangle oldScrollableBounds;
  private Rectangle newScrollableBounds;
  private boolean treeEvent;
  private int suspendCount;

  BoundsReconciliation( Composite adapter, Scrollable scrollable ) {
    this.adapter = adapter;
    this.scrollable = scrollable;
    registerListeners( scrollable );
    updateBoundsBuffer();
  }

  @Override
  public void controlMoved( ControlEvent e ) {
    if( !isSuspended() ) {
      newScrollableBounds = scrollable.getBounds();
    }
  }

  @Override
  public void controlResized( ControlEvent e ) {
    if( !isSuspended() ) {
      newScrollableBounds = scrollable.getBounds();
    }
  }

  @Override
  public void treeExpanded( TreeEvent e ) {
    treeEvent = true;
  }

  @Override
  public void treeCollapsed( TreeEvent e ) {
    treeEvent = true;
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
    adapter.setBounds( newScrollableBounds );
  }

  private void updateBoundsBuffer() {
    oldScrollableBounds = scrollable.getBounds();
    newScrollableBounds = scrollable.getBounds();
  }

  private boolean clearTreeEvent() {
    return treeEvent = false;
  }

  private void registerListeners( Scrollable scrollable ) {
    scrollable.addControlListener( this );
    if( scrollable instanceof Tree ) {
      ( ( Tree )scrollable ).addTreeListener( this );
    }
  }
}