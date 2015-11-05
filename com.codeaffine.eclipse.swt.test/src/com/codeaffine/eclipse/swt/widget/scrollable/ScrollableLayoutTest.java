package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.Reconciliation;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

public class ScrollableLayoutTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollBarConfigurer horizontalBarConfigurer;
  private ScrollableLayouter treeLayouter;
  private OverlayLayouter overlayLayouter;
  private Reconciliation reconciliation;
  private AdaptionContext<Tree> context;
  private ScrollableLayout layout;
  private Tree tree;

  @Before
  public void setUp() {
    overlayLayouter = mock( OverlayLayouter.class );
    horizontalBarConfigurer = mock( ScrollBarConfigurer.class );
    treeLayouter = mock( ScrollableLayouter.class );
    tree = createTree( createShell( displayHelper ), 6, 4 );
    context = new AdaptionContext<>( tree.getParent(), new ScrollableControl<>( tree ) );
    reconciliation = ReconciliationHelper.stubReconciliation();
    layout = new ScrollableLayout( context, overlayLayouter, treeLayouter, horizontalBarConfigurer, reconciliation );
  }

  @Test
  public void layout() {
     layout.layout( null, true );

     verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
     verify( overlayLayouter ).layout( any( AdaptionContext.class ) );
     verify( treeLayouter ).layout( any( AdaptionContext.class ) );
     verifyNoMoreInteractions( overlayLayouter, horizontalBarConfigurer, treeLayouter );
  }

  @Test
  public void layoutIfHorizontalBarIsVisible() {
    expandTopBranch( tree );
    context.updatePreferredSize();

    layout.layout( null, true );

    verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    verify( overlayLayouter ).layout( any( AdaptionContext.class ) );
    verify( treeLayouter ).layout( any( AdaptionContext.class ) );
    verify( horizontalBarConfigurer ).configure( any( AdaptionContext.class ) );
    verifyNoMoreInteractions( overlayLayouter, horizontalBarConfigurer, treeLayouter );
  }

  @Test
  public void ensureReconciliationClamp() {
    doNothing().when( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    expandTopBranch( tree );
    context.updatePreferredSize();

    layout.layout( null, true );

    verify( reconciliation ).runWhileSuspended( any( Runnable.class ) );
    verify( overlayLayouter, never() ).layout( any( AdaptionContext.class ) );
    verify( treeLayouter, never() ).layout( any( AdaptionContext.class ) );
    verify( horizontalBarConfigurer, never() ).configure( any( AdaptionContext.class ) );
    verifyNoMoreInteractions( overlayLayouter, horizontalBarConfigurer, treeLayouter );
  }

  @Test
  public void computeSize() {
    Point actual = layout.computeSize( null, SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ) );
  }
}