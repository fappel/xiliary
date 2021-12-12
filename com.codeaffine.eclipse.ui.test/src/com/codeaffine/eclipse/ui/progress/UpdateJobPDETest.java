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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.junit.Before;
import org.junit.Test;

public class UpdateJobPDETest {

  private ViewerAdapter viewerAdapter;
  private UpdateJob updateJob;
  private JobHelper jobHelper;
  private Object[] children;
  private Object parent;

  @Before
  public void setUp() {
    parent = new Object();
    children = new Object[]{ new Object() };
    viewerAdapter = mock( ViewerAdapter.class );
    updateJob = new UpdateJob( viewerAdapter, parent, children );
    jobHelper = new JobHelper( updateJob );
  }

  @Test
  public void schedule() {
    updateJob.schedule();
    jobHelper.waitTillJobHasFinished();

    verify( viewerAdapter ).addElements( parent, children );
    assertThat( updateJob.getResult() ).isSameAs( Status.OK_STATUS );
  }

  @Test
  public void scheduleIfViewerAdapterIsDisposed() {
    when( viewerAdapter.isDisposed() ).thenReturn( true );

    updateJob.schedule();
    jobHelper.waitTillJobHasFinished();

    verify( viewerAdapter, never() ).addElements( parent, children );
    assertThat( updateJob.getResult() ).isSameAs( Status.CANCEL_STATUS );
  }

  @Test
  public void runInUIThreadWithCanceledMonitor() {
    IProgressMonitor monitor = new NullProgressMonitor();
    monitor.setCanceled( true );

    IStatus actual = updateJob.run( monitor );

    assertThat( actual ).isSameAs( Status.CANCEL_STATUS );
  }
}