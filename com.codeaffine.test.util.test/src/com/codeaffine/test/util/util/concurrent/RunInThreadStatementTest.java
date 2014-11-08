package com.codeaffine.test.util.util.concurrent;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrown;
import static java.lang.Thread.currentThread;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runners.model.Statement;

import com.codeaffine.test.util.lang.ThrowableCaptor.Actor;

public class RunInThreadStatementTest {

  @Test
  public void evaluate() throws Throwable {
    ThreadCaptor captor = new ThreadCaptor();
    RunInThreadStatement statement = createWithBase( captor );

    statement.evaluate();

    assertThat( captor.getExecutionThread() )
      .isNotNull()
      .isNotSameAs( currentThread() );
  }

  @Test
  public void evaluateOnFailure() throws Throwable {
    AssertionError expected = new AssertionError();
    final RunInThreadStatement statement = createWithBaseThatThrows( expected );

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        statement.evaluate();
      }
    } );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void evaluateOnException() throws Throwable {
    Exception expected = new Exception();
    final RunInThreadStatement statement = createWithBaseThatThrows( expected );

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        statement.evaluate();
      }
    } );

    assertThat( actual ).isSameAs( expected );
  }

  private static RunInThreadStatement createWithBaseThatThrows( Throwable toBeThrown ) throws Throwable {
    Statement base = mock( Statement.class );
    doThrow( toBeThrown ).when( base ).evaluate();
    return createWithBase( base );
  }

  private static RunInThreadStatement createWithBase( Statement base ) {
    return new RunInThreadStatement( base );
  }
}