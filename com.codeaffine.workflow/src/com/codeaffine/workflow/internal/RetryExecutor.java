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
package com.codeaffine.workflow.internal;

import com.codeaffine.workflow.definition.Activity;
import com.codeaffine.workflow.definition.ActivityAspects;
import com.codeaffine.workflow.definition.Retry;

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