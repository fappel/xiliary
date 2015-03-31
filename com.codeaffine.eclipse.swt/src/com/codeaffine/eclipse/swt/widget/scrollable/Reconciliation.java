package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

class Reconciliation {

  final VisibilityReconciliation visibilityReconciliation;
  final BoundsReconciliation boundsReconciliation;
  final LayoutReconciliation layoutReconciliation;

  Reconciliation( Composite adapter, Scrollable scrollable ) {
    this( new VisibilityReconciliation( adapter, scrollable ),
          new BoundsReconciliation( adapter, scrollable ),
          new LayoutReconciliation( adapter, scrollable ) );
  }

  Reconciliation( VisibilityReconciliation visibilityReconciliation,
                  BoundsReconciliation boundsReconciliation,
                  LayoutReconciliation layoutReconciliation  )
  {
    this.visibilityReconciliation = visibilityReconciliation;
    this.boundsReconciliation = boundsReconciliation;
    this.layoutReconciliation = layoutReconciliation;
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
    layoutReconciliation.run();
  }
}