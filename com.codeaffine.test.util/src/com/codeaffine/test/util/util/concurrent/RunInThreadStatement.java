package com.codeaffine.test.util.util.concurrent;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

import org.junit.runners.model.Statement;

class RunInThreadStatement extends Statement {

  private final RunInThreadSynchronizer synchronizer;
  private final Statement baseStatement;

  private volatile Throwable throwable;

  RunInThreadStatement( Statement baseStatement ) {
    this.synchronizer = new RunInThreadSynchronizer();
    this.baseStatement = baseStatement;
  }

  @Override
  public void evaluate() throws Throwable {
    synchronizer.run( newSingleThreadExecutor(), new Runnable() {
      @Override
      public void run() {
        doEvaluate();
      }
    } );
    rethrowProblem();
  }

  private void doEvaluate() {
    try {
      baseStatement.evaluate();
    } catch( Throwable throwable ) {
      this.throwable = throwable;
    }
  }

  private void rethrowProblem() throws Throwable {
    if( throwable != null ) {
      throw throwable;
    }
  }
}