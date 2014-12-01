package com.codeaffine.eclipse.ui.progress;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;

class FetchJob extends Job {

  private final DeferredContentManager contentManager;
  private final ElementCollector collector;
  private final Object parent;

  FetchJob( DeferredContentManager manager, Object parent, PendingUpdatePlaceHolder placeHolder ) {
    super( getName( parent, manager.getAdapter( parent ) ) );
    this.collector = new ElementCollector( manager, parent, placeHolder );
    this.contentManager = manager;
    this.parent = parent;
    setRule( manager.getAdapter( parent ).getRule( parent ) );
    addJobChangeListener( new PlaceHolderCleanupTrigger( manager, placeHolder ) );
  }

  @Override
  public IStatus run( IProgressMonitor monitor ) {
    contentManager.getAdapter( parent ).fetchDeferredChildren( parent, collector, monitor );
    if( monitor.isCanceled() ) {
      return Status.CANCEL_STATUS;
    }
    return Status.OK_STATUS;
  }

  @Override
  public boolean belongsTo( Object family ) {
    return new BelongToJobFamilyChecker( contentManager, parent ).check( family );
  }

  private static String getName( Object parent, IDeferredWorkbenchAdapter adapter ) {
    return NLS.bind( ProgressMessages.DeferredTreeContentManager_FetchingName, adapter.getLabel( parent ) );
  }
}