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