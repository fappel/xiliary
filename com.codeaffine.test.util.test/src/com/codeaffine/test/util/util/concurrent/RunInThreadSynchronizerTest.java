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

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static java.lang.Thread.currentThread;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;

public class RunInThreadSynchronizerTest {

  private RunInThreadSynchronizer synchronizer;
  private ExecutorService executor;

  @Before
  public void setUp() {
    synchronizer = new RunInThreadSynchronizer();
    executor = Executors.newSingleThreadExecutor();
  }

  @Test
  public void run() {
    ThreadCaptor captor = new ThreadCaptor();

    synchronizer.run( executor, captor );

    assertThat( executor.isShutdown() ).isTrue();
    assertThat( captor.getExecutionThread() )
      .isNotNull()
      .isNotSameAs( currentThread() );
  }

  @Test
  public void runWithProblem() {
    RuntimeException expected = new RuntimeException();
    Runnable runnable = stubRunnableWithProblem( expected );

    Throwable actual = thrownBy( () -> synchronizer.run( executor, runnable ) );

    assertThat( executor.isShutdown() ).isTrue();
    assertThat( actual.getCause().getCause() ).isSameAs( expected );
    assertThat( actual )
      .isInstanceOf( IllegalStateException.class )
      .hasCauseInstanceOf( ExecutionException.class );
  }

  private static Runnable stubRunnableWithProblem( RuntimeException expected ) {
    final Runnable result = mock( Runnable.class );
    doThrow( expected ).when( result ).run();
    return result;
  }
}