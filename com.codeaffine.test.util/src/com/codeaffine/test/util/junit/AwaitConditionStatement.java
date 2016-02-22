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
package com.codeaffine.test.util.junit;

import static java.lang.String.format;

import org.junit.runners.model.Statement;

import com.codeaffine.test.util.junit.AwaitConditionRule.AwaitCondition;

class AwaitConditionStatement extends Statement {

  private static final String INTERRUPTION_PATTERN = "Interuption while waiting for condition '%s' to get true.";
  private static final String TIMEOUT_PATTERN = "Timeout while waiting for condition '%s' to get true.";

  private final AwaitCondition condition;
  private final int timeout;

  public AwaitConditionStatement( int timeout, AwaitCondition condition ) {
    this.timeout = timeout;
    this.condition = condition;
  }

  @Override
  public void evaluate() throws Throwable {
    long end = System.currentTimeMillis() + timeout;
    while( !timedOut( end ) && !condition.isSatisfied() ) {
      sleep();
    }
    if( timedOut( end ) ) {
      throw new AssertionError( format( TIMEOUT_PATTERN, condition.getClass().getName() ) );
    }
  }

  private void sleep() throws AssertionError {
    try {
      Thread.sleep( 100 );
    } catch( InterruptedException e ) {
      throw new AssertionError( format( INTERRUPTION_PATTERN, condition.getClass().getName() ) );
    }
  }

  private static boolean timedOut( long end ) {
    return end <= System.currentTimeMillis();
  }
}