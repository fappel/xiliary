package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.Reconciliation;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

@SuppressWarnings( "rawtypes" )
public class ScrollableLayoutTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollBarConfigurer horizontalBarConfigurer;
  private ScrollableLayouter scrollableLayouter;
  private OverlayLayouter overlayLayouter;
  private Reconciliation reconciliation;
  private AdaptionContext<Tree> context;
  private ScrollableLayout layout;
  private Tree tree;

  @Before
  public void setUp() {
    overlayLayouter = mock( OverlayLayouter.class );
    horizontalBarConfigurer = mock( ScrollBarConfigurer.class );
    scrollableLayouter = mock( ScrollableLayouter.class );
    tree = createTree( createShell( displayHelper ), 6, 4 );
    context = new AdaptionContext<>( tree.getParent(), new ScrollableControl<>( tree ) );
    reconciliation = ReconciliationHelper.stubReconciliation();
    layout = new ScrollableLayout(
      context, overlayLayouter, scrollableLayouter, horizontalBarConfigurer, reconciliation
    );
  }

  @Test
  public void layout() {
    ArgumentCaptor<AdaptionContext> captor = forClass( AdaptionContext.class );

    layout.layout( null, true );

    InOrder order = order();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( scrollableLayouter ).layout( captor.capture() );
    order.verify( overlayLayouter ).layout( captor.capture() );
    order.verifyNoMoreInteractions();
    assertContextGetsUpdated( captor );
  }

  @Test
  public void layoutIfHorizontalBarIsVisible() {
    expandTopBranch( tree );
    context.updatePreferredSize();

    layout.layout( null, true );

    InOrder order = order();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( scrollableLayouter ).layout( any( AdaptionContext.class ) );
    order.verify( overlayLayouter ).layout( any( AdaptionContext.class ) );
    order.verify( horizontalBarConfigurer ).configure( any( AdaptionContext.class ) );
    order.verifyNoMoreInteractions();
  }

  @Test
  public void ensureReconciliationClamp() {
    doNothing().when( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    expandTopBranch( tree );
    context.updatePreferredSize();

    layout.layout( null, true );

    InOrder order = order();
    order.verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    order.verify( overlayLayouter, never() ).layout( any( AdaptionContext.class ) );
    order.verify( scrollableLayouter, never() ).layout( any( AdaptionContext.class ) );
    order.verify( horizontalBarConfigurer, never() ).configure( any( AdaptionContext.class ) );
    order.verifyNoMoreInteractions();
  }

  @Test
  public void computeSize() {
    Point actual = layout.computeSize( null, SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ) );
  }

  private InOrder order() {
    return inOrder( reconciliation, overlayLayouter, scrollableLayouter, horizontalBarConfigurer );
  }


  private void assertContextGetsUpdated( ArgumentCaptor<AdaptionContext> captor ) {
    List<AdaptionContext> captured = captor.getAllValues();
    captured.forEach( actual -> assertThat( actual ).isNotSameAs( context ) );
    captured.forEach( actual -> assertThat( filterForActual( captured, actual ) ).hasSize( 1 ) );
  }

  private static List<AdaptionContext> filterForActual( List<AdaptionContext> captured, AdaptionContext actual ) {
    return captured.stream().filter( current -> current == actual ).collect( toList() );
  }
}