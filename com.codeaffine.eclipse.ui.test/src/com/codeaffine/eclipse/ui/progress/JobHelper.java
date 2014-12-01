package com.codeaffine.eclipse.ui.progress;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static com.codeaffine.eclipse.ui.progress.ThreadHelper.sleep;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

class JobHelper extends JobChangeAdapter {

  private static final int RETRIES = 30;

  private boolean done;
  private int retries;

  JobHelper() {}

  JobHelper( Job job ) {
    job.addJobChangeListener( this );
  }

  @Override
  public void done( IJobChangeEvent event ) {
    done = true;
  }

  void waitTillJobHasFinished() {
    retries = RETRIES;
    while( mustRetry() ) {
      flushPendingEvents();
      decrementRetries();
      sleep( 100 );
    }
  }

  private boolean mustRetry() {
    return retries > 0 && !done;
  }

  private void decrementRetries() throws AssertionError {
    retries--;
    if( retries == 0 ) {
      throw new AssertionError( "Job Helper exceeded retries[" + RETRIES + "]" );
    }
  }
}