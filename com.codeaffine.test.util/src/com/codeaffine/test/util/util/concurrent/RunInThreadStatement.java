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