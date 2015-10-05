package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Tree;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class TreeLayoutFactory extends ScrollableLayoutFactory<Tree> {

  @Override
  public Layout create(
    AdaptionContext<Tree> context, FlatScrollBar horizontal, FlatScrollBar vertical, Label cornerOverlay )
  {
    return new ScrollableLayout( newContext( context ), horizontal, vertical, cornerOverlay );
  }

  @Override
  public SelectionListener createHorizontalSelectionListener( AdaptionContext<Tree> context ) {
    return new HorizontalSelectionListener( context );
  }

  @Override
  public SelectionListener createVerticalSelectionListener( AdaptionContext<Tree> context ) {
    return new TreeVerticalSelectionListener( context );
  }

  @Override
  public DisposeListener createWatchDog(
    AdaptionContext<Tree> context, FlatScrollBar horizontal, FlatScrollBar vertical )
  {
    TreeVerticalScrollBarUpdater updater = new TreeVerticalScrollBarUpdater( context.getScrollable(), vertical );
    return new WatchDog( newContext( context ), updater );
  }

  private static AdaptionContext<Tree> newContext( AdaptionContext<Tree> context ) {
    return context.newContext( context.getScrollable().getItemHeight() );
  }
}