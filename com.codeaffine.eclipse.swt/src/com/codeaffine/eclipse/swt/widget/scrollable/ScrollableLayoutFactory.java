package com.codeaffine.eclipse.swt.widget.scrollable;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

abstract class ScrollableLayoutFactory<T extends Scrollable> implements LayoutFactory<T> {

  private final ScrollBarFactory scrollBarFactory;

  private FlatScrollBar horizontalBar;
  private FlatScrollBar verticalBar;

  ScrollableLayoutFactory() {
    scrollBarFactory = new ScrollBarFactory();
  }

  public abstract Layout create( T scrollable, FlatScrollBar horizontal, FlatScrollBar vertical, Label cornerOverlay );
  public abstract SelectionListener createHorizontalSelectionListener( T scrollable );
  public abstract SelectionListener createVerticalSelectionListener( T scrollable );
  public abstract DisposeListener createWatchDog( T scrollable, FlatScrollBar horizontal, FlatScrollBar vertical );

  @Override
  public Layout create( Composite parent, T scrollable ) {
    parent.setBackgroundMode( SWT.INHERIT_DEFAULT );
    parent.setBackground( scrollable.getBackground() );
    horizontalBar = scrollBarFactory.create( parent, scrollable, SWT.HORIZONTAL );
    verticalBar = scrollBarFactory.create( parent, scrollable, SWT.VERTICAL );
    horizontalBar.addSelectionListener( createHorizontalSelectionListener( scrollable ) );
    verticalBar.addSelectionListener( createVerticalSelectionListener( scrollable ) );
    parent.addDisposeListener( createWatchDog( scrollable, horizontalBar, verticalBar ) );
    Label cornerOverlay = createCornerOverlay( parent );
    return create( scrollable, horizontalBar, verticalBar, cornerOverlay );
  }

  private static Label createCornerOverlay( Composite parent ) {
    Label result = new Label( parent, SWT.NONE );
    result.moveAbove( null );
    return result;
  }

  @Override
  public void setIncrementButtonLength( int incrementButtonLength ) {
    horizontalBar.setIncrementButtonLength( incrementButtonLength );
    verticalBar.setIncrementButtonLength( incrementButtonLength );
  }

  @Override
  public int getIncrementButtonLength() {
    return horizontalBar.getIncrementButtonLength();
  }

  @Override
  public void setIncrementColor( Color incrementColor ) {
    horizontalBar.setIncrementColor( incrementColor );
    verticalBar.setIncrementColor( incrementColor );
  }

  @Override
  public Color getIncrementColor() {
    return horizontalBar.getIncrementColor();
  }

  @Override
  public void setPageIncrementColor( Color pageIncrementColor ) {
    horizontalBar.setPageIncrementColor( pageIncrementColor );
    verticalBar.setPageIncrementColor( pageIncrementColor );
  }

  @Override
  public Color getPageIncrementColor() {
    return horizontalBar.getPageIncrementColor();
  }

  @Override
  public void setThumbColor( Color thumbColor ) {
    horizontalBar.setThumbColor( thumbColor );
    verticalBar.setThumbColor( thumbColor );
  }

  @Override
  public Color getThumbColor() {
    return horizontalBar.getThumbColor();
  }
}