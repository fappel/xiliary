package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.test.util.ShellHelper.createShell;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.createTree;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeHelper.expandTopBranch;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutFactory.TreeLayoutContextFactory;

public class ScrollableLayoutTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollBarConfigurer horizontalBarConfigurer;
  private ScrollableLayouter treeLayouter;
  private OverlayLayouter overlayLayouter;
  private ScrollableLayout<Tree> layout;
  private Tree tree;

  @Before
  public void setUp() {
    overlayLayouter = mock( OverlayLayouter.class );
    horizontalBarConfigurer = mock( ScrollBarConfigurer.class );
    treeLayouter = mock( ScrollableLayouter.class );
    tree = createTree( createShell( displayHelper ), 6, 4 );
    TreeLayoutContextFactory contextFactory = new TreeLayoutContextFactory( tree );
    layout = new ScrollableLayout<Tree>( tree, contextFactory, overlayLayouter, treeLayouter, horizontalBarConfigurer );
  }

  @Test
  public void layout() {
     layout.layout( null, true );

     verify( overlayLayouter ).layout( any( LayoutContext.class ) );
     verify( treeLayouter ).layout( any( LayoutContext.class ) );
     verifyNoMoreInteractions( overlayLayouter, horizontalBarConfigurer, treeLayouter );
  }

  @Test
  public void layoutIfHorizontalBarIsVisible() {
    expandTopBranch( tree );

    layout.layout( null, true );

    verify( overlayLayouter ).layout( any( LayoutContext.class ) );
    verify( treeLayouter ).layout( any( LayoutContext.class ) );
    verify( horizontalBarConfigurer ).configure( any( LayoutContext.class ) );
    verifyNoMoreInteractions( overlayLayouter, horizontalBarConfigurer, treeLayouter );
  }

  @Test
  public void computeSize() {
    Point actual = layout.computeSize( null, SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ) );
  }
}