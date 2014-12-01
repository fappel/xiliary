package com.codeaffine.eclipse.ui.progress;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

class PlaceHolderCleanupTrigger extends JobChangeAdapter {

  private final PendingUpdatePlaceHolder placeHolder;
  private final DeferredContentManager contentManager;

  PlaceHolderCleanupTrigger( DeferredContentManager contentManager, PendingUpdatePlaceHolder placeHolder ) {
    this.contentManager = contentManager;
    this.placeHolder = placeHolder;
  }

  @Override
  public void done( IJobChangeEvent event ) {
    contentManager.clearPlaceholder( placeHolder );
  }
}