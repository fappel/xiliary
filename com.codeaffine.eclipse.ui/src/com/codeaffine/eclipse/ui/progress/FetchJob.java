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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

class FetchJob extends Job {

  private final DeferredContentManager contentManager;
  private final ElementCollector collector;
  private final Object parent;

  FetchJob( DeferredContentManager manager, Object parent, PendingUpdatePlaceHolder placeHolder ) {
    super( manager.getFetchJobName( parent, manager.getAdapter( parent ) ) );
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
}