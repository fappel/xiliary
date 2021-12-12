/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.ui.progress;

import java.util.Iterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.ui.progress.WorkbenchJob;

class ClearJob extends WorkbenchJob {

  private final PendingUpdatePlaceHolder placeholder;
  private final ViewerAdapter viewerAdapter;

  ClearJob( CompletionObserverList observers, ViewerAdapter viewerAdapter, PendingUpdatePlaceHolder placeholder ) {
    super( ProgressMessages.DeferredTreeContentManager_ClearJob );
    this.viewerAdapter = viewerAdapter;
    this.placeholder = placeholder;
    setSystem( true );
    register( observers );
  }

  @Override
  public IStatus runInUIThread( IProgressMonitor monitor ) {
    if( needsToBeRemoved() ) {
      removePlaceHolder();
      return Status.OK_STATUS;
    }
    return Status.CANCEL_STATUS;
  }

  private boolean needsToBeRemoved() {
    return !placeholder.isRemoved() && !viewerAdapter.isDisposed();
  }

  private void removePlaceHolder() {
    viewerAdapter.remove( placeholder );
    placeholder.setRemoved( true );
  }

  private void register( CompletionObserverList observerList ) {
    Iterator<IJobChangeListener> iterator = observerList.iterator();
    while( iterator.hasNext() ) {
      addJobChangeListener( iterator.next() );
    }
  }
}