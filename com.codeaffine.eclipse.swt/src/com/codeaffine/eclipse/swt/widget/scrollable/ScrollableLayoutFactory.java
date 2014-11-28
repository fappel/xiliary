package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

abstract class ScrollableLayoutFactory<T extends Scrollable> implements LayoutFactory<T> {

  private final ScrollBarFactory scrollBarFactory;

  ScrollableLayoutFactory() {
    scrollBarFactory = new ScrollBarFactory();
  }

  public abstract Layout create( T scrollable, FlatScrollBar horizontal, FlatScrollBar vertical );
  public abstract SelectionListener createHorizontalSelectionListener( T scrollable );
  public abstract SelectionListener createVerticalSelectionListener( T scrollable );
  public abstract DisposeListener createWatchDog( T scrollable, FlatScrollBar horizontal, FlatScrollBar vertical );

  @Override
  public Layout create( Composite parent, T scrollable ) {
    parent.setBackgroundMode( SWT.INHERIT_DEFAULT );
    parent.setBackground( scrollable.getBackground() );
    FlatScrollBar horizontalBar = scrollBarFactory.create( parent, scrollable, SWT.HORIZONTAL );
    FlatScrollBar verticalBar = scrollBarFactory.create( parent, scrollable, SWT.VERTICAL );
    horizontalBar.addSelectionListener( createHorizontalSelectionListener( scrollable ) );
    verticalBar.addSelectionListener( createVerticalSelectionListener( scrollable ) );
    parent.addDisposeListener( createWatchDog( scrollable, horizontalBar, verticalBar ) );
    return create( scrollable, horizontalBar, verticalBar );
  }


}
