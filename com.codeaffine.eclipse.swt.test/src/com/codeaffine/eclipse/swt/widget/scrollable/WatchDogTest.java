package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.ReconciliationHelper.stubReconciliation;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.codeaffine.eclipse.swt.util.ActionScheduler;


public class WatchDogTest {

  private TreeVerticalScrollBarUpdater settingCopier;
  private Reconciliation reconciliation;
  private Visibility hScrollVisibility;
  private Visibility vScrollVisibility;
  private ActionScheduler scheduler;
  private LayoutTrigger layoutTrigger;
  private TreeWidth treeWidth;
  private WatchDog watchDog;

  @Before
  public void setUp() {
    settingCopier = mock( TreeVerticalScrollBarUpdater.class );
    hScrollVisibility = mock( Visibility.class );
    vScrollVisibility = mock( Visibility.class );
    scheduler = mock( ActionScheduler.class );
    layoutTrigger = mock( LayoutTrigger.class );
    treeWidth = mock( TreeWidth.class );
    reconciliation = stubReconciliation();
    watchDog = new WatchDog(
      settingCopier, hScrollVisibility, vScrollVisibility, scheduler, layoutTrigger, treeWidth, reconciliation
    );
  }

  @Test
  public void initialSchedule() {
    verify( scheduler ).schedule( WatchDog.DELAY );
  }

  @Test
  public void run() {
    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( vScrollVisibility ).hasChanged();
    order.verify( hScrollVisibility ).hasChanged();
    order.verify( treeWidth ).hasScrollEffectingChange();
    order.verify( treeWidth ).update();
    order.verify( vScrollVisibility ).update();
    order.verify( hScrollVisibility ).update();
    order.verify( vScrollVisibility ).isVisible();
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void runIfVerticalScrollVisibilityHasChanged() {
    when( vScrollVisibility.hasChanged() ).thenReturn( true );

    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( vScrollVisibility ).hasChanged();
    order.verify( layoutTrigger ).pull();
    order.verify( treeWidth ).update();
    order.verify( vScrollVisibility ).update();
    order.verify( hScrollVisibility ).update();
    order.verify( vScrollVisibility ).isVisible();
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void runIfHorizontalScrollVisibilityHasChanged() {
    when( hScrollVisibility.hasChanged() ).thenReturn( true );

    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( vScrollVisibility ).hasChanged();
    order.verify( hScrollVisibility ).hasChanged();
    order.verify( layoutTrigger ).pull();
    order.verify( treeWidth ).update();
    order.verify( vScrollVisibility ).update();
    order.verify( hScrollVisibility ).update();
    order.verify( vScrollVisibility ).isVisible();
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void runIfTreeWidthHasScrollEffectingChange() {
    when( treeWidth.hasScrollEffectingChange() ).thenReturn( true );

    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( vScrollVisibility ).hasChanged();
    order.verify( hScrollVisibility ).hasChanged();
    order.verify( treeWidth ).hasScrollEffectingChange();
    order.verify( layoutTrigger ).pull();
    order.verify( treeWidth ).update();
    order.verify( vScrollVisibility ).update();
    order.verify( hScrollVisibility ).update();
    order.verify( vScrollVisibility ).isVisible();
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void runIfVerticalScrollBarIsVisible() {
    when( vScrollVisibility.isVisible() ).thenReturn( true );

    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( vScrollVisibility ).hasChanged();
    order.verify( hScrollVisibility ).hasChanged();
    order.verify( treeWidth ).hasScrollEffectingChange();
    order.verify( treeWidth ).update();
    order.verify( vScrollVisibility ).update();
    order.verify( hScrollVisibility ).update();
    order.verify( vScrollVisibility ).isVisible();
    order.verify( settingCopier ).update();
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void runAfterDisposal() {
    watchDog.widgetDisposed( null );

    watchDog.run();

    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void ensureReconciliationClamp() {
    doNothing().when( reconciliation ).runWhileSuspended( any( Runnable.class ) );

    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verify( vScrollVisibility, never() ).hasChanged();
    verify( hScrollVisibility, never() ).hasChanged();
    verify( treeWidth, never() ).hasScrollEffectingChange();
    verify( treeWidth, never() ).update();
    verify( vScrollVisibility, never() ).update();
    verify( hScrollVisibility, never() ).update();
    verify( vScrollVisibility, never() ).isVisible();
    verifyNoMoreInteractionOnDocs();
  }

  private InOrder docOrder() {
    return inOrder(
      settingCopier, hScrollVisibility, vScrollVisibility, scheduler, layoutTrigger, treeWidth, reconciliation
    );
  }

  private void verifyNoMoreInteractionOnDocs() {
    verifyNoMoreInteractions(
      settingCopier, hScrollVisibility, vScrollVisibility, layoutTrigger, treeWidth, reconciliation
    );
  }
}