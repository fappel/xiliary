package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class FlatScrollBarTreeLayout extends Layout {

  private final ScrollBarConfigurer horizontalBarConfigurer;
  private final TreeOverlayLayouter treeOverlayLayouter;
  private final TreeLayouter treeLayouter;
  private final Tree tree;

  FlatScrollBarTreeLayout( Tree tree, FlatScrollBar horizontal, FlatScrollBar vertical ) {
    this( new TreeOverlayLayouter( horizontal, vertical ),
          new ScrollBarConfigurer( horizontal ),
          new TreeLayouter( tree ),
          tree );
  }

  FlatScrollBarTreeLayout( TreeOverlayLayouter treeOverlayLayouter,
                           ScrollBarConfigurer scrollBarConfigurer,
                           TreeLayouter treeLayouter,
                           Tree tree )
  {
    this.treeOverlayLayouter = treeOverlayLayouter;
    this.horizontalBarConfigurer = scrollBarConfigurer;
    this.treeLayouter = treeLayouter;
    this.tree = tree;
  }

  @Override
  protected void layout( Composite composite, boolean flushCache ) {
    TreeLayoutContext context = new TreeLayoutContext( tree );
    treeOverlayLayouter.layout( context );
    treeLayouter.layout( context );
    if( context.isHorizontalBarVisible() ) {
      horizontalBarConfigurer.configure( context );
    }
  }

  @Override
  protected Point computeSize( Composite composite, int wHint, int hHint, boolean flushCache ) {
    return tree.computeSize( wHint, hHint, flushCache );
  }
}