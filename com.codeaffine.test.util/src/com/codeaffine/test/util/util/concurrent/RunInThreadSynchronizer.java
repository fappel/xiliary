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
package com.codeaffine.test.util.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class RunInThreadSynchronizer {

  void run( ExecutorService executorService , Runnable runnable  ) {
    Future<?> future = executorService.submit( runnable );
    try {
      waitTillFinished( future );
    } finally {
      executorService.shutdown();
    }
  }

  private static void waitTillFinished( Future<?> future ) {
    try {
      future.get();
    } catch( ExecutionException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    } catch( InterruptedException shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }
}