package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.util.ActionScheduler;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class WatchDog implements Runnable, DisposeListener {

  static final int DELAY = 10;

  private final Visibility vScrollVisibility;
  private final Visibility hScrollVisibility;
  private final VerticalScrollBarUpdater verticalSettingCopier;
  private final TreeWidth treeWidth;
  private final ActionScheduler scheduler;
  private final LayoutTrigger layoutTrigger;

  private boolean disposed;

  WatchDog( Tree tree, FlatScrollBar vertical  ) {
    this( new VerticalScrollBarUpdater( tree, vertical ),
          new Visibility( tree.getHorizontalBar() ),
          new Visibility( tree.getVerticalBar() ),
          null,
          new LayoutTrigger( tree.getParent() ),
          new TreeWidth( tree ) );
  }

  WatchDog( VerticalScrollBarUpdater settingCopier,
            Visibility hScrollVisibility,
            Visibility vScrollVisibility,
            ActionScheduler actionScheduler,
            LayoutTrigger layoutTrigger,
            TreeWidth treeWidth )
  {
    this.verticalSettingCopier = settingCopier;
    this.hScrollVisibility = hScrollVisibility;
    this.vScrollVisibility = vScrollVisibility;
    this.scheduler = ensureScheduler( actionScheduler );
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
      doRun();
      scheduler.schedule( DELAY );
    }
  }

  private void doRun() {
    if( vScrollVisibility.hasChanged() || hScrollVisibility.hasChanged() || treeWidth.hasScrollEffectingChange() ) {
      layoutTrigger.pull();
    }
    treeWidth.update();
    vScrollVisibility.update();
    hScrollVisibility.update();
    if( vScrollVisibility.isVisible() ) {
      verticalSettingCopier.update();
    }
  }

  private ActionScheduler ensureScheduler( ActionScheduler actionScheduler ) {
    return actionScheduler == null ? new ActionScheduler( Display.getDefault(), this ) : actionScheduler;
  }
}