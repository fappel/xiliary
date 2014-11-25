package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.Content;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class CustomContent implements Content {

  private final FlatScrollBarTreeLayout flatScrollBarTreeLayout;

  CustomContent( Composite parent, Tree tree ) {
    parent.setBackgroundMode( SWT.INHERIT_DEFAULT );
    parent.setBackground( tree.getBackground() );
    FlatScrollBar horizontalBar = createFlatScrollBar( parent, tree, SWT.HORIZONTAL );
    FlatScrollBar verticalBar = createFlatScrollBar( parent, tree, SWT.VERTICAL );
    parent.setBackground( tree.getBackground() );
    horizontalBar.addSelectionListener( new HorizontalSelectionListener( tree ) );
    verticalBar.addSelectionListener( new VerticalSelectionListener( tree ) );
    parent.addDisposeListener( new WatchDog( tree, verticalBar ) );
    flatScrollBarTreeLayout = new FlatScrollBarTreeLayout( tree, horizontalBar, verticalBar );
  }

  @Override
  public Layout getLayout() {
    return flatScrollBarTreeLayout;
  }

  private static FlatScrollBar createFlatScrollBar( Composite parent, Tree tree, int direction  ) {
    FlatScrollBar result = new FlatScrollBar( parent, direction );
    result.setBackground( tree.getBackground() );
    return result;
  }
}