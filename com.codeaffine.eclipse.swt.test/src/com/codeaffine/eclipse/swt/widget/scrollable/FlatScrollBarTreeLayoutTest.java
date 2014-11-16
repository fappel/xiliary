package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.testhelper.ShellHelper.createShell;
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


public class FlatScrollBarTreeLayoutTest {

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private TreeOverlayLayouter overlayLayouter;
  private ScrollBarConfigurer scrollBarConfigurer;
  private FlatScrollBarTreeLayout layout;
  private TreeLayouter treeLayouter;
  private Tree tree;

  @Before
  public void setUp() {
    overlayLayouter = mock( TreeOverlayLayouter.class );
    scrollBarConfigurer = mock( ScrollBarConfigurer.class );
    treeLayouter = mock( TreeLayouter.class );
    tree = createTree( createShell( displayHelper ), 6, 4 );
    layout = new FlatScrollBarTreeLayout( overlayLayouter, scrollBarConfigurer, treeLayouter, tree );
  }

  @Test
  public void layout() {
     layout.layout( null, true );

     verify( overlayLayouter ).layout( any( TreeLayoutContext.class ) );
     verify( treeLayouter ).layout( any( TreeLayoutContext.class ) );
     verifyNoMoreInteractions( overlayLayouter, scrollBarConfigurer, treeLayouter );
  }

  @Test
  public void layoutIfHorizontalBarIsVisible() {
    expandTopBranch( tree );

    layout.layout( null, true );

    verify( overlayLayouter ).layout( any( TreeLayoutContext.class ) );
    verify( treeLayouter ).layout( any( TreeLayoutContext.class ) );
    verify( scrollBarConfigurer ).configure( any( TreeLayoutContext.class ) );
    verifyNoMoreInteractions( overlayLayouter, scrollBarConfigurer, treeLayouter );
  }

  @Test
  public void computeSize() {
    Point actual = layout.computeSize( null, SWT.DEFAULT, SWT.DEFAULT, true );

    assertThat( actual ).isEqualTo( tree.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ) );
  }
}