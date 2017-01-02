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
package com.codeaffine.util.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class Synchronizer {

  private final Lock lock;

  public Synchronizer() {
    this( new ReentrantLock() );
  }

  Synchronizer( Lock lock ) {
    this.lock = lock;
  }

  public void execute( Runnable operation ) {
    lock.lock();
    try {
      operation.run();
    } finally {
      lock.unlock();
    }
  }

  public <T> T execute( Supplier<T> operation ) {
    lock.lock();
    try {
      return operation.get();
    } finally {
      lock.unlock();
    }
  }
}