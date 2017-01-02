/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.ui.progress;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.junit.Test;

public class ProgressServiceTest {

  @Test
  public void scheduleWithDelegate() {
    IWorkbenchSiteProgressService delegate = mock( IWorkbenchSiteProgressService.class );
    ProgressService progressService = new ProgressService( delegate );
    Job job = mock( Job.class );

    progressService.schedule( job );

    verify( delegate ).schedule( job );
    verify( job, never() ).schedule();
  }

  @Test
  public void scheduleWithoutDelegate() {
    ProgressService progressService = new ProgressService( null );
    Job job = mock( Job.class );

    progressService.schedule( job );

    verify( job ).schedule();
  }
}