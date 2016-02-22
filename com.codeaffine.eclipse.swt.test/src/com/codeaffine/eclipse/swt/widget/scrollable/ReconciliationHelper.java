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
package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.codeaffine.eclipse.swt.widget.scrollable.context.Reconciliation;

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