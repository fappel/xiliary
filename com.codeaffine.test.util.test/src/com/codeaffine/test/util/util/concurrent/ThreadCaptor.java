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