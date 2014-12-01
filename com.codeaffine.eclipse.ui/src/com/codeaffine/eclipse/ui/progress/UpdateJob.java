package com.codeaffine.eclipse.ui.progress;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.WorkbenchJob;

class UpdateJob extends WorkbenchJob {

  private final ViewerAdapter viewerAdapter;
  private final Object[] children;
  private final Object parent;

  UpdateJob( ViewerAdapter viewerAdapter, Object parent, Object[] children ) {
    super( ProgressMessages.DeferredTreeContentManager_AddingChildren );
    setSystem( true );
    this.viewerAdapter = viewerAdapter;
    this.children = children;
    this.parent = parent;
  }

  @Override
  public IStatus runInUIThread( IProgressMonitor monitor ) {
    if( isAlive( monitor ) ) {
      update();
      return Status.OK_STATUS;
    }
    return Status.CANCEL_STATUS;
  }

  private boolean isAlive( IProgressMonitor monitor ) {
    return !monitor.isCanceled() && !viewerAdapter.isDisposed();
  }

  private void update() {
    viewerAdapter.addElements( parent, children );
  }
}