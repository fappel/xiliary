package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

class Reconciliation {

  final VisibilityReconciliation visibilityReconciliation;
  final LayoutDataReconciliation layoutDataReconciliation;
  final BoundsReconciliation boundsReconciliation;

  Reconciliation( Composite adapter, Scrollable scrollable ) {
    this( new VisibilityReconciliation( adapter, scrollable ),
          new BoundsReconciliation( adapter, scrollable ),
          new LayoutDataReconciliation( adapter, scrollable ) );
  }

  Reconciliation( VisibilityReconciliation visibilityReconciliation,
                  BoundsReconciliation boundsReconciliation,
                  LayoutDataReconciliation layoutDataReconciliation )
  {
    this.visibilityReconciliation = visibilityReconciliation;
    this.boundsReconciliation = boundsReconciliation;
    this.layoutDataReconciliation = layoutDataReconciliation;
  }

  void runWithSuspendedBoundsReconciliation( Runnable runnable ) {
    boundsReconciliation.runSuspended( runnable );
  }

  boolean setVisible( boolean visible ) {
    return visibilityReconciliation.setVisible( visible );
  }

  void runWhileSuspended( Runnable runnable ) {
    suspend();
    try {
      runnable.run();
    } finally {
      resume();
    }
  }

  private void suspend() {
    boundsReconciliation.suspend();
  }

  private void resume() {
    visibilityReconciliation.run();
    boundsReconciliation.resume();
    boundsReconciliation.run();
    layoutDataReconciliation.run();
  }
}