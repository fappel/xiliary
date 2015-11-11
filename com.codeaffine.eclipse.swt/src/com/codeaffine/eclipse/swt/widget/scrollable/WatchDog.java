package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;

import com.codeaffine.eclipse.swt.util.ActionScheduler;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.Reconciliation;

class WatchDog implements Runnable, DisposeListener {

  static final int DELAY = 10;

  private final VerticalScrollBarUpdater verticalBarUpdater;
  private final NestingStructurePreserver nestingStructurePresever;
  private final Reconciliation reconciliation;
  private final Visibility vScrollVisibility;
  private final Visibility hScrollVisibility;
  private final LayoutTrigger layoutTrigger;
  private final ActionScheduler scheduler;
  private final AdaptionContext<?> context;
  private final TreeWidth treeWidth;

  boolean layoutInitialized;
  boolean disposed;

  WatchDog( AdaptionContext<?> context, VerticalScrollBarUpdater verticalUpdater ) {
    this( context,
          verticalUpdater,
          new Visibility( SWT.HORIZONTAL, context ),
          new Visibility( SWT.VERTICAL, context ),
          null,
          new LayoutTrigger( context.getAdapter() ),
          new TreeWidth( context ),
          context.getReconciliation(),
          new NestingStructurePreserver( context ) );
  }

  WatchDog( AdaptionContext<?> context,
            VerticalScrollBarUpdater verticalBarUpdater,
            Visibility hScrollVisibility,
            Visibility vScrollVisibility,
            ActionScheduler actionScheduler,
            LayoutTrigger layoutTrigger,
            TreeWidth treeWidth,
            Reconciliation reconciliation,
            NestingStructurePreserver nestingPresever )
  {
    this.context = context;
    this.verticalBarUpdater = verticalBarUpdater;
    this.hScrollVisibility = hScrollVisibility;
    this.vScrollVisibility = vScrollVisibility;
    this.nestingStructurePresever = nestingPresever;
    this.scheduler = ensureScheduler( actionScheduler );
    this.reconciliation = reconciliation;
    this.layoutTrigger = layoutTrigger;
    this.treeWidth = treeWidth;
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
    treeWidth.update();
    vScrollVisibility.update();
    hScrollVisibility.update();
    if( vScrollVisibility.isVisible() ) {
      verticalBarUpdater.update();
    }
    nestingStructurePresever.run();
  }

  private boolean mustLayout() {
    boolean result =    !layoutInitialized
                     || vScrollVisibility.hasChanged()
                     || hScrollVisibility.hasChanged()
                     || treeWidth.hasScrollEffectingChange();
    layoutInitialized = true;
    return result;
  }

  private ActionScheduler ensureScheduler( ActionScheduler actionScheduler ) {
    return actionScheduler == null ? new ActionScheduler( Display.getDefault(), this ) : actionScheduler;
  }
}