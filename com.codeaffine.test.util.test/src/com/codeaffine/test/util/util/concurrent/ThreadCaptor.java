package com.codeaffine.test.util.util.concurrent;

import org.junit.runners.model.Statement;

class ThreadCaptor extends Statement implements Runnable {

  private Thread executionThread;

  @Override
  public void evaluate() throws Throwable {
    capture();
  }

  @Override
  public void run() {
    capture();
  }

  private void capture() {
    executionThread = Thread.currentThread();
  }

  Thread getExecutionThread() {
    return executionThread;
  }
}