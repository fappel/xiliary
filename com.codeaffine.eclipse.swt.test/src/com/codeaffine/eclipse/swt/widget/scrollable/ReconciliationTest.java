package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.codeaffine.test.util.lang.ThrowableCaptor.Actor;

public class ReconciliationTest {

  private VisibilityReconciliation visibliltyReconciliation;
  private LayoutDataReconciliation layoutDataReconciliation;
  private BoundsReconciliation boundsReconciliation;
  private LayoutReconciliation layoutReconciliation;
  private Reconciliation reconciliation;

  @Before
  public void setUp() {
    visibliltyReconciliation = mock( VisibilityReconciliation.class );
    boundsReconciliation = mock( BoundsReconciliation.class );
    layoutDataReconciliation = mock( LayoutDataReconciliation.class );
    layoutReconciliation = mock( LayoutReconciliation.class );
    reconciliation = new Reconciliation(
      visibliltyReconciliation, boundsReconciliation, layoutDataReconciliation, layoutReconciliation
    );
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
    final Runnable runnable = stubRunnableWithProblem( expected );

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        reconciliation.runWhileSuspended( runnable );
      }
    } );

    InOrder order = order( runnable );
    verifyActionsBeforeRunnableExcecution( order );
    order.verify( runnable ).run();
    verifyActionsAfterRunnableExcecution( order );
    order.verifyNoMoreInteractions();
    assertThat( actual ).isSameAs( expected );
  }

  private InOrder order( Runnable runnable ) {
    return inOrder( runnable,
                    boundsReconciliation,
                    visibliltyReconciliation,
                    layoutDataReconciliation,
                    layoutReconciliation );
  }

  private void verifyActionsBeforeRunnableExcecution( InOrder order ) {
    order.verify( boundsReconciliation ).suspend();
  }

  private void verifyActionsAfterRunnableExcecution( InOrder order ) {
    order.verify( visibliltyReconciliation ).run();
    order.verify( boundsReconciliation ).resume();
    order.verify( boundsReconciliation ).run();
    order.verify( layoutDataReconciliation ).run();
    order.verify( layoutReconciliation ).run();
  }

  private static Runnable stubRunnableWithProblem( RuntimeException expected ) {
    Runnable result = mock( Runnable.class );
    doThrow( expected ).when( result ).run();
    return result;
  }
}