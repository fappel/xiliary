package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.codeaffine.eclipse.swt.util.ActionScheduler;


public class WatchDogTest {

  private TreeVerticalScrollBarUpdater settingCopier;
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
    watchDog = new WatchDog( settingCopier, hScrollVisibility, vScrollVisibility, scheduler, layoutTrigger, treeWidth );
  }

  @Test
  public void initialSchedule() {
    verify( scheduler ).schedule( WatchDog.DELAY );
  }

  @Test
  public void run() {
    watchDog.run();

    InOrder order = docOrder();
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

  private InOrder docOrder() {
    return inOrder( settingCopier, hScrollVisibility, vScrollVisibility, scheduler, layoutTrigger, treeWidth );
  }

  private void verifyNoMoreInteractionOnDocs() {
    verifyNoMoreInteractions( settingCopier, hScrollVisibility, vScrollVisibility, layoutTrigger, treeWidth );
  }
}