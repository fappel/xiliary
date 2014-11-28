package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

class ScrollableLayoutFactorySpy
  extends ScrollableLayoutFactory<Scrollable>
  implements SelectionListener, DisposeListener
{

  private SelectionEvent selectionEvent;
  private DisposeEvent disposeEvent;
  private FlatScrollBar horizontal;
  private FlatScrollBar vertical;
  private FillLayout layout;

  @Override
  public Layout create( Scrollable scrollable, FlatScrollBar horizontal, FlatScrollBar vertical ) {
    this.horizontal = horizontal;
    this.vertical = vertical;
    layout = new FillLayout();
    return layout;
  }

  @Override
  public SelectionListener createHorizontalSelectionListener( Scrollable scrollable ) {
    return this;
  }

  @Override
  public SelectionListener createVerticalSelectionListener( Scrollable scrollable ) {
    return this;
  }

  @Override
  public DisposeListener createWatchDog( Scrollable scrollable, FlatScrollBar horizontal, FlatScrollBar vertical ) {
    return this;
  }

  @Override
  public void widgetSelected( SelectionEvent selectionEvent ) {
    this.selectionEvent = selectionEvent;
  }

  @Override
  public void widgetDefaultSelected( SelectionEvent selectionEvent ) {
  }

  @Override
  public void widgetDisposed( DisposeEvent disposeEvent ) {
    this.disposeEvent = disposeEvent;
  }

  FlatScrollBar getHorizontal() {
    return horizontal;
  }

  FlatScrollBar getVertical() {
    return vertical;
  }

  SelectionEvent getSelectionEvent() {
    return selectionEvent;
  }

  DisposeEvent getDisposeEvent() {
    return disposeEvent;
  }

  Layout getLayout() {
    return layout;
  }
}