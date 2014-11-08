package com.codeaffine.test.util.util.concurrent;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrown;
import static java.lang.Thread.currentThread;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.test.util.lang.ThrowableCaptor.Actor;

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
    final Runnable runnable = stubRunnableWithProblem( expected );

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        synchronizer.run( executor, runnable );
      }
    } );

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