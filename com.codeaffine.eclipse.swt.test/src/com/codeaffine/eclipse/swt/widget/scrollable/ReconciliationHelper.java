package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class ReconciliationHelper {

  static Reconciliation stubReconciliation() {
    Reconciliation result = mock( Reconciliation.class );
    doAnswer( withRunnableExecution() ).when( result ).runWhileSuspended( any( Runnable.class ) );
    return result;
  }

  private static Answer<Object> withRunnableExecution() {
    return new Answer<Object>() {
      @Override
      public Object answer( InvocationOnMock invocation ) throws Throwable {
        Runnable runnable = ( Runnable )invocation.getArguments()[ 0 ];
        runnable.run();
        return null;
      }
    };
  }
}