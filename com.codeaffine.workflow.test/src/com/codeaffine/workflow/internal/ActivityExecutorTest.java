package com.codeaffine.workflow.internal;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.codeaffine.workflow.Activity;
import com.codeaffine.workflow.ActivityAspect;
import com.codeaffine.workflow.Retry;

public class ActivityExecutorTest {

  private static final int DELAY = 500;

  private Activity activity;
  private ActivityExecutor activityExecutor;

  static class RetryActivity implements Activity {

    private final Activity delegate;

    RetryActivity( Activity delegate ) {
      this.delegate = delegate;
    }

    @Override
    @Retry( on = IllegalStateException.class, delay = DELAY, times = 1 )
    public void execute() {
      delegate.execute();
    }
  }

  @Before
  public void setUp() {
    activity = mock( Activity.class );
    activityExecutor = new ActivityExecutor();
  }

  @Test
  public void testExecute() {
    activityExecutor.execute( activity );

    verify( activity ).execute();
  }

  @Test
  public void testExecuteWithProblem() {
    RuntimeException toBeThrown = equipActivityWithIllegalStateException();

    try {
      activityExecutor.execute( activity );
      fail();
    } catch( RuntimeException expected ) {
      assertSame( toBeThrown, expected );
    }
  }

  @Test
  public void testExecuteWithAspect() {
    ActivityAspect aspect = equipExecutorWithMockedAspect();

    activityExecutor.execute( activity );

    InOrder order = inOrder( aspect, activity );
    order.verify( aspect ).beforeExecute( activity );
    order.verify( activity ).execute();
    order.verify( aspect ).afterExecute( activity, null );
    order.verifyNoMoreInteractions();
  }

  @Test
  public void testExecuteWithRetryAnnotation() {
    ActivityAspect aspect = equipExecutorWithMockedAspect();
    RuntimeException expected = equipActivityWithIllegalStateException();
    RetryActivity retryActivity = new RetryActivity( activity );
    long start = System.currentTimeMillis();

    try {
      activityExecutor.execute( retryActivity );
      fail();
    } catch( RuntimeException actual ) {
      long duration = System.currentTimeMillis() - start;
      assertSame( expected, actual );
      assertTrue( duration >= DELAY );
      InOrder order = inOrder( aspect, activity );
      order.verify( aspect ).beforeExecute( retryActivity );
      order.verify( activity, times( 2 ) ).execute();
      order.verify( aspect ).afterExecute( retryActivity, expected );
      order.verifyNoMoreInteractions();
    }
  }

  @Test
  public void testExecuteWithRetryAnnotationAndUncaughtException() {
    ActivityAspect aspect = equipExecutorWithMockedAspect();
    RuntimeException expected = equipActivityWithRuntimeException();
    RetryActivity retryActivity = new RetryActivity( activity );
    long start = System.currentTimeMillis();

    try {
      activityExecutor.execute( retryActivity );
      fail();
    } catch( RuntimeException actual ) {
      long duration = System.currentTimeMillis() - start;
      assertTrue( duration < DELAY );
      assertSame( expected, actual );
      InOrder order = inOrder( aspect, activity );
      order.verify( aspect ).beforeExecute( retryActivity );
      order.verify( activity ).execute();
      order.verify( aspect ).afterExecute( retryActivity, expected );
      order.verifyNoMoreInteractions();
    }
  }

  @Test
  public void testExecuteWithAspectAndProblem() {
    RuntimeException toBeThrown = equipActivityWithIllegalStateException();
    ActivityAspect aspect = equipExecutorWithMockedAspect();

    try {
      activityExecutor.execute( activity );
      fail();
    } catch( RuntimeException expected ) {
      InOrder order = inOrder( aspect, activity );
      order.verify( aspect ).beforeExecute( activity );
      order.verify( activity ).execute();
      order.verify( aspect ).afterExecute( activity, toBeThrown );
      order.verifyNoMoreInteractions();
      assertSame( toBeThrown, expected );
    }
  }

  @Test
  public void testExecuteWithProblemInBeforeExecute() {
    ActivityAspect aspect = equipExecutorWithMockedAspect();
    RuntimeException toBeThrown = equipBeforeExecuteWithProblem( aspect );

    try {
      activityExecutor.execute( activity );
      fail();
    } catch( RuntimeException expected ) {
      InOrder order = inOrder( aspect, activity );
      order.verify( aspect ).beforeExecute( activity );
      order.verifyNoMoreInteractions();
      assertSame( toBeThrown, expected );
    }
  }

  @Test
  public void testExecuteWithProblemInAfterExecute() {
    ActivityAspect aspect = equipExecutorWithMockedAspect();
    RuntimeException toBeThrown = equipAfterExecuteWithProblem( aspect );

    try {
      activityExecutor.execute( activity );
      fail();
    } catch( RuntimeException expected ) {
      InOrder order = inOrder( aspect, activity );
      order.verify( aspect ).beforeExecute( activity );
      order.verify( activity ).execute();
      order.verify( aspect ).afterExecute( activity, null );
      order.verifyNoMoreInteractions();
      assertSame( toBeThrown, expected );
    }
  }

  private RuntimeException equipActivityWithIllegalStateException() {
    return equipActivityWithProblem( new IllegalStateException( "Problem in execute." ) );
  }

  private RuntimeException equipActivityWithRuntimeException() {
    return equipActivityWithProblem( new RuntimeException( "Problem in execute." ) );
  }

  private RuntimeException equipActivityWithProblem( RuntimeException result ) {
    doThrow( result ).when( activity ).execute();
    return result;
  }

  private ActivityAspect equipExecutorWithMockedAspect() {
    ActivityAspect result = mock( ActivityAspect.class );
    activityExecutor.add( result );
    return result;
  }

  private RuntimeException equipBeforeExecuteWithProblem( ActivityAspect aspect ) {
    RuntimeException result = new RuntimeException( "Problem in beforeExecute." );
    doThrow( result ).when( aspect ).beforeExecute( activity );
    return result;
  }

  private RuntimeException equipAfterExecuteWithProblem( ActivityAspect aspect ) {
    RuntimeException result = new RuntimeException( "Problem in afterExecute." );
    doThrow( result ).when( aspect ).afterExecute( activity, null );
    return result;
  }
}