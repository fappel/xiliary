package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.test.util.lang.ThrowableCaptor.Actor;

public class BoundsReconciliationTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private BoundsReconciliation reconciliation;
  private Shell adapter;
  private Tree scrollable;

  @Before
  public void setUp() {
    adapter = createShell( displayHelper );
    scrollable = TreeHelper.createTree( adapter, 1, 1 );
    reconciliation = new BoundsReconciliation( adapter, scrollable );
  }

  @Test
  public void run() {
    Rectangle expected = adapter.getBounds();

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  public void runAfterScrollableBoundsHaveBeenChanged() {
    Rectangle expected = new Rectangle( 100, 200, 300, 400 );
    scrollable.setBounds( expected );

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  public void runAfterScrollableBoundsHaveChangedByTreeExpansion() {
    Rectangle expected = adapter.getBounds();
    scrollable.setBounds( new Rectangle( 100, 200, 300, 400 ) );
    reconciliation.treeExpanded( null );

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  public void runAfterScrollableBoundsHaveChangedByTreeCollaps() {
    Rectangle expected = adapter.getBounds();
    scrollable.setBounds( new Rectangle( 100, 200, 300, 400 ) );
    reconciliation.treeCollapsed( null );

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  public void subsequentRunWithoutChange() {
    Rectangle expected = adapter.getBounds();
    scrollable.setBounds( new Rectangle( 100, 200, 300, 400 ) );
    reconciliation.treeExpanded( null );
    reconciliation.run();

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  public void subsequentRunWithChange() {
    Rectangle expected = new Rectangle( 10, 20, 100, 500 );
    scrollable.setBounds( new Rectangle( 100, 200, 300, 400 ) );
    reconciliation.treeExpanded( null );
    reconciliation.run();
    scrollable.setBounds( expected );

    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  public void runSuspended() {
    Rectangle expected = adapter.getBounds();

    reconciliation.runSuspended( new Runnable() {
      @Override
      public void run() {
        scrollable.setBounds( new Rectangle( 100, 200, 300, 400 ) );
      }
    } );
    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
  }

  @Test
  public void runSuspendedWithProblem() {
    Rectangle expected = new Rectangle( 100, 200, 300, 400 );
    final RuntimeException toBeThrown = new RuntimeException();

    Throwable problem = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        reconciliation.runSuspended( stubRunnableWithProblem( toBeThrown ) );
      }
    } );
    scrollable.setBounds( expected );
    reconciliation.run();

    assertThat( adapter.getBounds() ).isEqualTo( expected );
    assertThat( problem ).isSameAs( toBeThrown );
  }

  private static Runnable stubRunnableWithProblem( Throwable problem ) {
    Runnable result = mock( Runnable.class );
    doThrow( problem ).when( result ).run();
    return result;
  }
}