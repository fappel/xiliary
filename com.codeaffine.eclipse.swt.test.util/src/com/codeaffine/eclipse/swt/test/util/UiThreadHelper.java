/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.test.util;

import static com.codeaffine.eclipse.swt.test.util.DisplayHelper.flushPendingEvents;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class UiThreadHelper {

  public static void runInOwnThreadWithReadAndDispatch( Runnable task ) {
    ExecutorService executor = newSingleThreadExecutor();
    try {
      Future<?> future = executor.submit( task );
      while( !future.isDone() ) {
        flushPendingEvents();
      }
      future.get();
    } catch( RuntimeException rte ) {
      throw rte;
    } catch( ExecutionException ee ) {
      throw new IllegalStateException( ee.getCause() );
    } catch( Exception e ) {
      throw new IllegalStateException( e );
    } finally {
      executor.shutdown();
    }
  }
}