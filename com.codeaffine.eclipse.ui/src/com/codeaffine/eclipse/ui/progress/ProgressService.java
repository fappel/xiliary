package com.codeaffine.eclipse.ui.progress;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;

class ProgressService {

  private final IWorkbenchSiteProgressService delegate;

  ProgressService( IWorkbenchSiteProgressService delegate ) {
    this.delegate = delegate;
  }

  void schedule( Job job ) {
    if( delegate == null ) {
      job.schedule();
    } else {
      delegate.schedule( job );
    }
  }
}