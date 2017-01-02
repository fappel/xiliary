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

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class SynchronizerTest {

  private Synchronizer synchronizer;
  private Lock lock;

  @Before
  public void setUp() {
    lock = mock( Lock.class );
    synchronizer = new Synchronizer( lock );
  }

  @Test
  public void executeWithRunnable() {
    Runnable runnable = mock( Runnable.class );

    synchronizer.execute( runnable );

    verifyRunnableLockHandlingOnExecute( runnable );
  }

  @Test
  public void executeWithExceptionThrowingRunnable() {
    RuntimeException expected = new RuntimeException();
    Runnable runnable = mock( Runnable.class );
    doThrow( expected ).when( runnable ).run();

    Throwable actual = thrownBy( () -> synchronizer.execute( runnable ) );

    assertSame( expected, actual );
    verifyRunnableLockHandlingOnExecute( runnable );
  }

  @Test
  public void executeWithExceptionThrowingSupplier() {
    RuntimeException expected = new RuntimeException();
    Supplier<Object> supplier = stubSupplier( new Object() );
    doThrow( expected ).when( supplier ).get();

    Throwable actual = thrownBy( () -> synchronizer.execute( supplier ) );

    assertSame( expected, actual );
    verifyRunnableLockHandlingOnExecute( supplier );
  }

  @Test
  public void executeWithSupplier() {
    Object expected = new Object();
    Supplier<Object> supplier = stubSupplier( expected );

    Object actual = synchronizer.execute( supplier );

    assertSame( expected, actual );
    verifyRunnableLockHandlingOnExecute( supplier );
  }

  @SuppressWarnings("unchecked")
  private static Supplier<Object> stubSupplier( Object expected ) {
    Supplier<Object> result = mock( Supplier.class );
    when( result.get() ).thenReturn( expected );
    return result;
  }

  private void verifyRunnableLockHandlingOnExecute( Runnable runnable ) {
    InOrder order = inOrder( lock, runnable );
    order.verify( lock ).lock();
    order.verify( runnable ).run();
    order.verify( lock ).unlock();
    order.verifyNoMoreInteractions();
  }

  private void verifyRunnableLockHandlingOnExecute( Supplier<?> supplier ) {
    InOrder order = inOrder( lock, supplier );
    order.verify( lock ).lock();
    order.verify( supplier ).get();
    order.verify( lock ).unlock();
    order.verifyNoMoreInteractions();
  }
}