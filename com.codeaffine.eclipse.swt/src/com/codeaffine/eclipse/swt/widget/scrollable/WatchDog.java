package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;

import com.codeaffine.eclipse.swt.util.ActionScheduler;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.Reconciliation;

class WatchDog implements Runnable, DisposeListener {

  static final int DELAY = 50;

  private final NestingStructurePreserver nestingStructurePresever;
  private final ScrollBarUpdater horizontalBarUpdater;
  private final ScrollBarUpdater verticalBarUpdater;
  private final Reconciliation reconciliation;
  private final Visibility vScrollVisibility;
  private final Visibility hScrollVisibility;
  private final LayoutTrigger layoutTrigger;
  private final SizeObserver sizeObserver;
  private final ActionScheduler scheduler;

  AdaptionContext<?> context;
  boolean layoutInitialized;
  boolean disposed;

  WatchDog( AdaptionContext<?> context,
            ScrollBarUpdater horizontalUpdater,
            ScrollBarUpdater verticalUpdater,
            SizeObserver sizeObserver )
  {
    this( context,
          horizontalUpdater,
          verticalUpdater,
          new Visibility( SWT.HORIZONTAL ),
          new Visibility( SWT.VERTICAL ),
          null,
          new LayoutTrigger( context.getAdapter() ),
          sizeObserver,
          context.getReconciliation(),
          new NestingStructurePreserver( context ) );
  }

  WatchDog( AdaptionContext<?> context,
            ScrollBarUpdater horizontalBarUpdater,
            ScrollBarUpdater verticalBarUpdater,
            Visibility hScrollVisibility,
            Visibility vScrollVisibility,
            ActionScheduler actionScheduler,
            LayoutTrigger layoutTrigger,
            SizeObserver sizeObserver,
            Reconciliation reconciliation,
            NestingStructurePreserver nestingPresever )
  {
    this.context = context;
    this.horizontalBarUpdater = horizontalBarUpdater;
    this.verticalBarUpdater = verticalBarUpdater;
    this.hScrollVisibility = hScrollVisibility;
    this.vScrollVisibility = vScrollVisibility;
    this.nestingStructurePresever = nestingPresever;
    this.scheduler = ensureScheduler( actionScheduler );
    this.reconciliation = reconciliation;
    this.layoutTrigger = layoutTrigger;
    this.sizeObserver = sizeObserver;
    scheduler.schedule( DELAY );
  }

  @Override
  public void widgetDisposed( DisposeEvent e ) {
    disposed = true;
  }

  @Override
  public void run() {
    if( !disposed ) {
      runWithReconciliationSuspended();
      scheduler.schedule( DELAY );
    }
  }

  private void runWithReconciliationSuspended() {
    reconciliation.runWhileSuspended( () -> doRun() );
  }

  private void doRun() {
    context.updatePreferredSize();
    if( mustLayout() ) {
      layoutTrigger.pull();
    }
    context = context.newContext();
    sizeObserver.update( context );
    vScrollVisibility.update( context );
    hScrollVisibility.update( context );
    if( vScrollVisibility.isVisible() ) {
      verticalBarUpdater.update();
    }
    if( hScrollVisibility.isVisible() ) {
      horizontalBarUpdater.update();
    }
    nestingStructurePresever.run();
  }

  private boolean mustLayout() {
    boolean result =    !layoutInitialized
                     || vScrollVisibility.hasChanged( context )
                     || hScrollVisibility.hasChanged( context )
                     || sizeObserver.mustLayoutAdapter( context );
    layoutInitialized = true;
    return result;
  }

  private ActionScheduler ensureScheduler( ActionScheduler actionScheduler ) {
    return actionScheduler == null ? new ActionScheduler( Display.getDefault(), this ) : actionScheduler;
  }
}