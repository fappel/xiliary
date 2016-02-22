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

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.core.runtime.Status.OK_STATUS;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;
import org.junit.Before;
import org.junit.Test;

public class FetchJobPDETest {

  private DeferredContentManager contentManager;
  private PendingUpdatePlaceHolder placeHolder;
  private IDeferredWorkbenchAdapter adapter;
  private JobHelper jobHelper;
  private FetchJob fetchJob;
  private TestItem parent;

  @Before
  public void setUp() {
    parent = new TestItem( null, "parent" );
    placeHolder = new PendingUpdatePlaceHolder();
    adapter = spy( new TestItemAdapter() );
    contentManager = stubContentManager( adapter );
    fetchJob = new FetchJob( contentManager, parent, placeHolder );
    jobHelper = new JobHelper( fetchJob );
  }

  @Test
  public void schedule() {
    fetchJob.schedule();
    jobHelper.waitTillJobHasFinished();

    verify( adapter )
      .fetchDeferredChildren( eq( parent ), any( ElementCollector.class ), any( IProgressMonitor.class ) );
    verify( contentManager )
      .clearPlaceholder( placeHolder );
    assertThat( fetchJob.getResult() )
      .isSameAs( OK_STATUS );
  }

  @Test
  public void runWithCanceledMonitor() {
    NullProgressMonitor monitor = new NullProgressMonitor();
    monitor.setCanceled( true );

    IStatus actual = fetchJob.run( monitor );

    assertThat( actual ).isSameAs( Status.CANCEL_STATUS );
    verify( adapter )
      .fetchDeferredChildren( eq( parent ), any( ElementCollector.class ), eq( monitor ) );
  }

  private DeferredContentManager stubContentManager( IDeferredWorkbenchAdapter adapter ) {
    DeferredContentManager result = mock( DeferredContentManager.class );
    when( result.getAdapter( parent ) ).thenReturn( adapter );
    when( result.getFetchJobName( anyObject(), any( IDeferredWorkbenchAdapter.class ) ) ).thenReturn( "Fetching.." );
    return result;
  }
}