package com.codeaffine.workflow.internal;

import com.codeaffine.workflow.Activity;
import com.codeaffine.workflow.ActivityAspects;
import com.codeaffine.workflow.Retry;

class RetryExecutor {

  private final Activity activity;
  private int loopsLeft = 1;
  private int delay = 0;
  private Class<? extends RuntimeException> exceptionType = RuntimeException.class;

  public RetryExecutor( Activity activity ) {
    Retry annotation = ActivityAspects.getAnnotation( activity, Retry.class );
    this.activity = activity;
    this.loopsLeft = annotation == null ? 1 : annotation.times() + 1;
    this.delay = annotation == null ? 0: annotation.delay();
    this.exceptionType = annotation == null ? RuntimeException.class : annotation.on();
  }

  void execute() {
    do {
      RuntimeException problem = null;
      try {
        activity.execute();
      } catch( RuntimeException rte ) {
        problem = rte;
      }
      handleProblem( problem );
      sleepLoopDelayTime();
    } while( loopsLeft > 0 );
  }

  private void handleProblem( RuntimeException problem ) {
    if( problem == null ) {
      loopsLeft = 0;
    } else if( exceptionType.isAssignableFrom( problem.getClass() ) && loopsLeft > 1 ) {
      loopsLeft--;
    } else {
      throw problem;
    }
  }

  private void sleepLoopDelayTime() {
    if( loopsLeft > 0 ) {
      try {
        Thread.sleep( delay );
      } catch( InterruptedException shouldNotHappen ) {
        throw new IllegalStateException( shouldNotHappen );
      }
    }
  }
}