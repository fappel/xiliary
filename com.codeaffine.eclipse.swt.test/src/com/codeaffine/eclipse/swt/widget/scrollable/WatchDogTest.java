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

import org.eclipse.swt.widgets.Scrollable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.codeaffine.eclipse.swt.util.ActionScheduler;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.Reconciliation;

public class WatchDogTest {

  private NestingStructurePreserver nestingStructurePreserver;
  private ScrollBarUpdater horizontalScrollBarUpdater;
  private ScrollBarUpdater verticalScrollBarUpdater;
  private AdaptionContext<Scrollable> context;
  private Reconciliation reconciliation;
  private Visibility hScrollVisibility;
  private Visibility vScrollVisibility;
  private ActionScheduler scheduler;
  private LayoutTrigger layoutTrigger;
  private SizeObserver sizeObserver;
  private WatchDog watchDog;

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    context = mock( AdaptionContext.class );
    horizontalScrollBarUpdater = mock( ScrollBarUpdater.class );
    verticalScrollBarUpdater = mock( ScrollBarUpdater.class );
    hScrollVisibility = mock( Visibility.class );
    vScrollVisibility = mock( Visibility.class );
    scheduler = mock( ActionScheduler.class );
    layoutTrigger = mock( LayoutTrigger.class );
    sizeObserver = mock( SizeObserver.class );
    reconciliation = stubReconciliation();
    nestingStructurePreserver = mock( NestingStructurePreserver.class );
    watchDog = new WatchDog( context,
                             horizontalScrollBarUpdater,
                             verticalScrollBarUpdater,
                             hScrollVisibility,
                             vScrollVisibility,
                             scheduler,
                             layoutTrigger,
                             sizeObserver,
                             reconciliation,
                             nestingStructurePreserver );
    watchDog.layoutInitialized = true;
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
    order.verify( context ).updatePreferredSize();
    order.verify( vScrollVisibility ).hasChanged();
    order.verify( hScrollVisibility ).hasChanged();
    order.verify( sizeObserver ).mustLayoutAdapter();
    order.verify( sizeObserver ).update();
    order.verify( vScrollVisibility ).update();
    order.verify( hScrollVisibility ).update();
    order.verify( vScrollVisibility ).isVisible();
    order.verify( hScrollVisibility ).isVisible();
    order.verify( nestingStructurePreserver ).run();
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void runWithLayoutInitialization() {
    watchDog.layoutInitialized = false;

    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( context ).updatePreferredSize();
    order.verify( layoutTrigger ).pull();
    order.verify( sizeObserver ).update();
    order.verify( vScrollVisibility ).update();
    order.verify( hScrollVisibility ).update();
    order.verify( vScrollVisibility ).isVisible();
    order.verify( hScrollVisibility ).isVisible();
    order.verify( nestingStructurePreserver ).run();
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void runIfVerticalScrollVisibilityHasChanged() {
    when( vScrollVisibility.hasChanged() ).thenReturn( true );

    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( context ).updatePreferredSize();
    order.verify( vScrollVisibility ).hasChanged();
    order.verify( layoutTrigger ).pull();
    order.verify( sizeObserver ).update();
    order.verify( vScrollVisibility ).update();
    order.verify( hScrollVisibility ).update();
    order.verify( vScrollVisibility ).isVisible();
    order.verify( hScrollVisibility ).isVisible();
    order.verify( nestingStructurePreserver ).run();
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void runIfHorizontalScrollVisibilityHasChanged() {
    when( hScrollVisibility.hasChanged() ).thenReturn( true );

    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( context ).updatePreferredSize();
    order.verify( vScrollVisibility ).hasChanged();
    order.verify( hScrollVisibility ).hasChanged();
    order.verify( layoutTrigger ).pull();
    order.verify( sizeObserver ).update();
    order.verify( vScrollVisibility ).update();
    order.verify( hScrollVisibility ).update();
    order.verify( vScrollVisibility ).isVisible();
    order.verify( hScrollVisibility ).isVisible();
    order.verify( nestingStructurePreserver ).run();
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void runIfSizeObserverRequestsAdapterLayout() {
    when( sizeObserver.mustLayoutAdapter() ).thenReturn( true );

    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( context ).updatePreferredSize();
    order.verify( vScrollVisibility ).hasChanged();
    order.verify( hScrollVisibility ).hasChanged();
    order.verify( sizeObserver ).mustLayoutAdapter();
    order.verify( layoutTrigger ).pull();
    order.verify( sizeObserver ).update();
    order.verify( vScrollVisibility ).update();
    order.verify( hScrollVisibility ).update();
    order.verify( vScrollVisibility ).isVisible();
    order.verify( hScrollVisibility ).isVisible();
    order.verify( nestingStructurePreserver ).run();
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void runIfVerticalScrollBarIsVisible() {
    when( vScrollVisibility.isVisible() ).thenReturn( true );

    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( context ).updatePreferredSize();
    order.verify( vScrollVisibility ).hasChanged();
    order.verify( hScrollVisibility ).hasChanged();
    order.verify( sizeObserver ).mustLayoutAdapter();
    order.verify( sizeObserver ).update();
    order.verify( vScrollVisibility ).update();
    order.verify( hScrollVisibility ).update();
    order.verify( vScrollVisibility ).isVisible();
    order.verify( verticalScrollBarUpdater ).update();
    order.verify( hScrollVisibility ).isVisible();
    order.verify( nestingStructurePreserver ).run();
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void runIfHorizontalScrollBarIsVisible() {
    when( hScrollVisibility.isVisible() ).thenReturn( true );

    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( context ).updatePreferredSize();
    order.verify( vScrollVisibility ).hasChanged();
    order.verify( hScrollVisibility ).hasChanged();
    order.verify( sizeObserver ).mustLayoutAdapter();
    order.verify( sizeObserver ).update();
    order.verify( vScrollVisibility ).update();
    order.verify( hScrollVisibility ).update();
    order.verify( vScrollVisibility ).isVisible();
    order.verify( hScrollVisibility ).isVisible();
    order.verify( horizontalScrollBarUpdater ).update();
    order.verify( nestingStructurePreserver ).run();
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
    order.verify( context, never() ).updatePreferredSize();
    verify( vScrollVisibility, never() ).hasChanged();
    verify( hScrollVisibility, never() ).hasChanged();
    verify( sizeObserver, never() ).mustLayoutAdapter();
    verify( sizeObserver, never() ).update();
    verify( vScrollVisibility, never() ).update();
    verify( hScrollVisibility, never() ).update();
    verify( nestingStructurePreserver, never() ).run();
    verify( vScrollVisibility, never() ).isVisible();
    verify( hScrollVisibility, never() ).isVisible();
    verifyNoMoreInteractionOnDocs();
  }

  private InOrder docOrder() {
    return inOrder( context,
                    horizontalScrollBarUpdater,
                    verticalScrollBarUpdater,
                    hScrollVisibility,
                    vScrollVisibility,
                    scheduler,
                    layoutTrigger,
                    sizeObserver,
                    reconciliation,
                    nestingStructurePreserver );
  }

  private void verifyNoMoreInteractionOnDocs() {
    verifyNoMoreInteractions(
      horizontalScrollBarUpdater,
      verticalScrollBarUpdater,
      hScrollVisibility,
      vScrollVisibility,
      layoutTrigger,
      sizeObserver,
      reconciliation,
      nestingStructurePreserver
    );
  }
}