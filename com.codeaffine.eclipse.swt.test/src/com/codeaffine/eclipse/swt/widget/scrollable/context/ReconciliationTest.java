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
package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.eclipse.swt.widgets.Composite;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

public class ReconciliationTest {

  private VisibilityReconciliation visibliltyReconciliation;
  private EnablementReconciliation enablementReconciliation;
  private BoundsReconciliation boundsReconciliation;
  private LayoutReconciliation layoutReconciliation;
  private ColorReconciliation colorReconciliation;
  private Reconciliation reconciliation;

  @Before
  public void setUp() {
    visibliltyReconciliation = mock( VisibilityReconciliation.class );
    enablementReconciliation = mock( EnablementReconciliation.class );
    boundsReconciliation = mock( BoundsReconciliation.class );
    layoutReconciliation = mock( LayoutReconciliation.class );
    colorReconciliation = mock( ColorReconciliation.class );
    reconciliation = new Reconciliation( visibliltyReconciliation,
                                         enablementReconciliation,
                                         boundsReconciliation,
                                         layoutReconciliation,
                                         colorReconciliation );
  }

  @Test
  public void runWithSuspendedBoundsReconciliation() {
    Runnable runnable = mock( Runnable.class );

    reconciliation.runWithSuspendedBoundsReconciliation( runnable );

    verify( boundsReconciliation ).runSuspended( runnable );
  }

  @Test
  public void setVisible() {
    boolean expected = true;
    when( visibliltyReconciliation.setVisible( expected ) ).thenReturn( expected );

    boolean actual = reconciliation.setVisible( expected );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void setEnabled() {
    boolean expected = true;
    when( enablementReconciliation.setEnabled( expected ) ).thenReturn( expected );

    boolean actual = reconciliation.setEnabled( expected );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void runWhileSuspended() {
    Runnable runnable = mock( Runnable.class );

    reconciliation.runWhileSuspended( runnable );

    InOrder order = order( runnable );
    verifyActionsBeforeRunnableExcecution( order );
    order.verify( runnable ).run();
    verifyActionsAfterRunnableExcecution( order );
    order.verifyNoMoreInteractions();
  }

  @Test
  public void runWhileSuspendedWithProblemOnRunnableExecution() {
    RuntimeException expected = new RuntimeException();
    Runnable runnable = stubRunnableWithProblem( expected );

    Throwable actual = thrownBy( () ->  reconciliation.runWhileSuspended( runnable ) );

    InOrder order = order( runnable );
    verifyActionsBeforeRunnableExcecution( order );
    order.verify( runnable ).run();
    verifyActionsAfterRunnableExcecution( order );
    order.verifyNoMoreInteractions();
    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void castToScrollbarStyleIfPossible() {
    Composite expected = mock( Composite.class, withSettings().extraInterfaces( ScrollbarStyle.class ) );

    ScrollbarStyle actual = Reconciliation.castToScrollbarStyleIfPossible( expected );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void castToScrollbarStyleIfNotPossible() {
    Composite composite = mock( Composite.class );

    ScrollbarStyle actual = Reconciliation.castToScrollbarStyleIfPossible( composite );

    assertThat( actual ).isNull();
  }

  private InOrder order( Runnable runnable ) {
    return
      inOrder( runnable,
               boundsReconciliation,
               visibliltyReconciliation,
               enablementReconciliation,
               layoutReconciliation,
               colorReconciliation );
  }

  private void verifyActionsBeforeRunnableExcecution( InOrder order ) {
    order.verify( boundsReconciliation ).suspend();
  }

  private void verifyActionsAfterRunnableExcecution( InOrder order ) {
    order.verify( visibliltyReconciliation ).run();
    order.verify( enablementReconciliation ).run();
    order.verify( boundsReconciliation ).resume();
    order.verify( boundsReconciliation ).run();
    order.verify( layoutReconciliation ).run();
    order.verify( colorReconciliation ).run();
  }

  private static Runnable stubRunnableWithProblem( RuntimeException expected ) {
    Runnable result = mock( Runnable.class );
    doThrow( expected ).when( result ).run();
    return result;
  }
}