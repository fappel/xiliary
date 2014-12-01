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