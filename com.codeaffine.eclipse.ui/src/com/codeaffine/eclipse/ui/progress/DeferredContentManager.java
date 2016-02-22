/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.ui.progress;

import static org.eclipse.core.runtime.Assert.isNotNull;

import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;

public class DeferredContentManager {

  private static final Object[] EMPTY_CHILDREN = new Object[ 0 ];

  private final PendingUpdatePlaceHolderFactory placeHolderFactory;
  private final CompletionObserverList completionObservers;
  private final ProgressService progressService;
  private final ViewerAdapter viewerAdapter;
  private final Adapters adapters;

  public DeferredContentManager( ViewerAdapter viewerAdapter ) {
    this( viewerAdapter, new DefaultPlaceHolderFactory(), null );
  }

  public DeferredContentManager( ViewerAdapter viewerAdapter, IWorkbenchPartSite site ) {
    this( viewerAdapter, new DefaultPlaceHolderFactory(), site );
  }

  public DeferredContentManager( ViewerAdapter viewerAdapter, PendingUpdatePlaceHolderFactory placeHolderFactory ) {
    this( viewerAdapter, placeHolderFactory, null );
  }

  public DeferredContentManager(
    ViewerAdapter viewerAdapter, PendingUpdatePlaceHolderFactory placeHolderFactory, IWorkbenchPartSite site )
  {
    this.adapters = new Adapters();
    this.completionObservers = new CompletionObserverList();
    this.progressService = lookupProgressService( adapters, site );
    this.placeHolderFactory = placeHolderFactory;
    this.viewerAdapter = viewerAdapter;
  }

  public boolean mayHaveChildren( Object element ) {
    isNotNull( element, ProgressMessages.DeferredTreeContentManager_NotDeferred );

    IDeferredWorkbenchAdapter adapter = getAdapter( element );
    return adapter != null && adapter.isContainer();
  }

  public Object[] getChildren( Object parent ) {
    if( isDeferredAdapter( parent ) ) {
      return startDeferredFetchingOfChildren( parent );
    }
    return EMPTY_CHILDREN;
  }

  public boolean isDeferredAdapter( Object element ) {
    return getAdapter( element ) != null;
  }

  public void cancel( Object parent ) {
    if( parent != null ) {
      Job.getJobManager().cancel( new DeferredContentJobFamily( this, parent ) );
    }
  }

  public void addUpdateCompleteListener( IJobChangeListener listener ) {
    completionObservers.add( listener );
  }

  public void removeUpdateCompleteListener( IJobChangeListener listener ) {
    completionObservers.remove( listener );
  }

  protected IDeferredWorkbenchAdapter getAdapter( Object element ) {
    return adapters.getAdapter( element, IDeferredWorkbenchAdapter.class );
  }

  private void startDeferredFetchingOfChildren( Object parent, PendingUpdatePlaceHolder placeHolder ) {
    cancel( parent );
    progressService.schedule( new FetchJob( this, parent, placeHolder ) );
  }

  protected void addChildren( Object parent, Object[] children ) {
    new UpdateJob( viewerAdapter, parent, children ).schedule();
  }

  protected void clearPlaceholder( PendingUpdatePlaceHolder placeHolder ) {
    if( !placeHolder.isRemoved() && PlatformUI.isWorkbenchRunning() ) {
      new ClearJob( completionObservers, viewerAdapter, placeHolder ).schedule();
    }
  }

  protected String getFetchJobName( Object parent, IDeferredWorkbenchAdapter adapter ) {
    return NLS.bind( ProgressMessages.DeferredTreeContentManager_FetchingName, adapter.getLabel( parent ) );
  }


  private Object[] startDeferredFetchingOfChildren( Object parent ) {
    PendingUpdatePlaceHolder placeholder = placeHolderFactory.create();
    startDeferredFetchingOfChildren( parent, placeholder );
    return new Object[] { placeholder };
  }

  private static ProgressService lookupProgressService( Adapters adapters, IWorkbenchPartSite site ) {
    return new ProgressService( adapters.getAdapter( site, IWorkbenchSiteProgressService.class ) );
  }
}