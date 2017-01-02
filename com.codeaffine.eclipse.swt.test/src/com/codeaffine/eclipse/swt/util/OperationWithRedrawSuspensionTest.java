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
package com.codeaffine.eclipse.swt.util;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import org.eclipse.swt.widgets.Control;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class OperationWithRedrawSuspensionTest {

  private OperationWithRedrawSuspension operation;
  private Control control;
  private Runnable runnable;

  @Before
  public void setUp() {
    operation = new OperationWithRedrawSuspension();
    control = mock( Control.class );
    runnable = mock( Runnable.class );
  }

  @Test
  public void execute() {
    operation.execute( control, runnable );

    verifyEmbeddedExecutionOrder();
  }

  @Test
  public void executeWithFailureOnRunnableInvocation() {
    RuntimeException expected = new RuntimeException( "bad" );
    doThrow( expected ).when( runnable ).run();

    Throwable actual = thrownBy( () -> operation.execute( control, runnable ) );

    assertThat( actual ).isSameAs( actual );
    verifyEmbeddedExecutionOrder();
  }

  private void verifyEmbeddedExecutionOrder() {
    InOrder order = inOrder( control, runnable );
    order.verify( control ).setRedraw( false );
    order.verify( runnable ).run();
    order.verify( control ).setRedraw( true );
    order.verifyNoMoreInteractions();
  }
}