package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.ReconciliationHelper.stubReconciliation;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
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
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.Reconciliation;

public class WatchDogTest {

  private NestingStructurePreserver nestingStructurePreserver;
  private ScrollBarUpdater horizontalScrollBarUpdater;
  private ScrollBarUpdater verticalScrollBarUpdater;
  private AdaptionContext<?> context;
  private Reconciliation reconciliation;
  private Visibility hScrollVisibility;
  private Visibility vScrollVisibility;
  private ActionScheduler scheduler;
  private LayoutTrigger layoutTrigger;
  private SizeObserver sizeObserver;
  private WatchDog watchDog;

  @Before
  public void setUp() {
    context = stubAdaptionContext();
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
    order.verify( vScrollVisibility ).hasChanged( context );
    order.verify( hScrollVisibility ).hasChanged( context );
    order.verify( sizeObserver ).mustLayoutAdapter( context );
    order.verify( sizeObserver ).update( any( AdaptionContext.class ) );
    order.verify( vScrollVisibility ).update( any( AdaptionContext.class ) );
    order.verify( hScrollVisibility ).update( any( AdaptionContext.class ) );
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
    order.verify( sizeObserver ).update( any( AdaptionContext.class ) );
    order.verify( vScrollVisibility ).update( any( AdaptionContext.class ) );
    order.verify( hScrollVisibility ).update( any( AdaptionContext.class ) );
    order.verify( vScrollVisibility ).isVisible();
    order.verify( hScrollVisibility ).isVisible();
    order.verify( nestingStructurePreserver ).run();
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void runIfVerticalScrollVisibilityHasChanged() {
    when( vScrollVisibility.hasChanged( context ) ).thenReturn( true );

    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( context ).updatePreferredSize();
    order.verify( vScrollVisibility ).hasChanged( context );
    order.verify( layoutTrigger ).pull();
    order.verify( sizeObserver ).update( any( AdaptionContext.class ) );
    order.verify( vScrollVisibility ).update( any( AdaptionContext.class ) );
    order.verify( hScrollVisibility ).update( any( AdaptionContext.class ) );
    order.verify( vScrollVisibility ).isVisible();
    order.verify( hScrollVisibility ).isVisible();
    order.verify( nestingStructurePreserver ).run();
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void runIfHorizontalScrollVisibilityHasChanged() {
    when( hScrollVisibility.hasChanged( context ) ).thenReturn( true );

    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( context ).updatePreferredSize();
    order.verify( vScrollVisibility ).hasChanged( context );
    order.verify( hScrollVisibility ).hasChanged( context );
    order.verify( layoutTrigger ).pull();
    order.verify( sizeObserver ).update( any( AdaptionContext.class ) );
    order.verify( vScrollVisibility ).update( any( AdaptionContext.class ) );
    order.verify( hScrollVisibility ).update( any( AdaptionContext.class ) );
    order.verify( vScrollVisibility ).isVisible();
    order.verify( hScrollVisibility ).isVisible();
    order.verify( nestingStructurePreserver ).run();
    order.verify( scheduler ).schedule( WatchDog.DELAY );
    verifyNoMoreInteractionOnDocs();
  }

  @Test
  public void runIfSizeObserverRequestsAdapterLayout() {
    when( sizeObserver.mustLayoutAdapter( context ) ).thenReturn( true );

    watchDog.run();

    InOrder order = docOrder();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( context ).updatePreferredSize();
    order.verify( vScrollVisibility ).hasChanged( context );
    order.verify( hScrollVisibility ).hasChanged( context );
    order.verify( sizeObserver ).mustLayoutAdapter( context );
    order.verify( layoutTrigger ).pull();
    order.verify( sizeObserver ).update( any( AdaptionContext.class ) );
    order.verify( vScrollVisibility ).update( any( AdaptionContext.class ) );
    order.verify( hScrollVisibility ).update( any( AdaptionContext.class ) );
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
    order.verify( vScrollVisibility ).hasChanged( context );
    order.verify( hScrollVisibility ).hasChanged( context );
    order.verify( sizeObserver ).mustLayoutAdapter( context );
    order.verify( sizeObserver ).update( any( AdaptionContext.class ) );
    order.verify( vScrollVisibility ).update( any( AdaptionContext.class ) );
    order.verify( hScrollVisibility ).update( any( AdaptionContext.class ) );
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
    order.verify( vScrollVisibility ).hasChanged( context );
    order.verify( hScrollVisibility ).hasChanged( context );
    order.verify( sizeObserver ).mustLayoutAdapter( context );
    order.verify( sizeObserver ).update( any( AdaptionContext.class ) );
    order.verify( vScrollVisibility ).update( any( AdaptionContext.class ) );
    order.verify( hScrollVisibility ).update( any( AdaptionContext.class ) );
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
    verify( vScrollVisibility, never() ).hasChanged( context );
    verify( hScrollVisibility, never() ).hasChanged( context );
    verify( sizeObserver, never() ).mustLayoutAdapter( context );
    verify( sizeObserver, never() ).update( any( AdaptionContext.class ) );
    verify( vScrollVisibility, never() ).update( any( AdaptionContext.class ) );
    verify( hScrollVisibility, never() ).update( any( AdaptionContext.class ) );
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

  private AdaptionContext<?> stubAdaptionContext() {
    AdaptionContext<?> result = mock( AdaptionContext.class );
    doAnswer( invocation -> stubAdaptionContext() ).when( result ).newContext();
    return result;
  }
}