package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TreeLayoutFactory extends ScrollableLayoutFactory<Tree> {

  static class TreeLayoutContextFactory implements LayoutContextFactory {

    private final Tree tree;

    TreeLayoutContextFactory( Tree tree ) {
      this.tree = tree;
    }

    @Override
    public LayoutContext create() {
      return new LayoutContext( tree, tree.getItemHeight() );
    }
  }

  @Override
  public Layout create( Tree tree, FlatScrollBar horizontal, FlatScrollBar vertical ) {
    return new ScrollableLayout<Tree>( tree, createContextFactory( tree ), horizontal, vertical );
  }

  @Override
  public SelectionListener createHorizontalSelectionListener( Tree tree ) {
    return new HorizontalSelectionListener( tree );
  }

  @Override
  public SelectionListener createVerticalSelectionListener( Tree tree ) {
    return new TreeVerticalSelectionListener( tree );
  }

  @Override
  public DisposeListener createWatchDog( Tree tree, FlatScrollBar horizontal, FlatScrollBar vertical ) {
    return new WatchDog( tree, createContextFactory( tree ), new TreeVerticalScrollBarUpdater( tree, vertical ) );
  }

  private static TreeLayoutContextFactory createContextFactory( Tree tree ) {
    return new TreeLayoutContextFactory( tree );
  }
}