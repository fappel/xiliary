package com.codeaffine.eclipse.swt.widget.scrollable;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Widget;

import com.codeaffine.eclipse.swt.util.ControlReflectionUtil;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

abstract class ScrollableLayoutFactory<T extends Scrollable> implements LayoutFactory<T> {

  static final String SCROLL_BAR_ADAPTER_CLASS = "org.eclipse.swt.widgets.ScrollBarAdapter";

  private final ControlReflectionUtil reflectionUtil;

  private FlatScrollBar horizontalBar;
  private ScrollBar horizontalAdapter;
  private FlatScrollBar verticalBar;
  private ScrollBar verticalAdapter;
  private Label cornerOverlay;

  ScrollableLayoutFactory() {
    reflectionUtil = new ControlReflectionUtil();
  }

  public abstract Layout create( AdaptionContext<T> context, FlatScrollBar horizontal, FlatScrollBar vertical );
  public abstract DisposeListener createWatchDog( AdaptionContext<T> context, FlatScrollBar horizontal, FlatScrollBar vertical );
  public abstract SelectionListener createHorizontalSelectionListener( AdaptionContext<T> context );
  public abstract SelectionListener createVerticalSelectionListener( AdaptionContext<T> context );

  @Override
  public Layout create( AdaptionContext<T> context ) {
    context.getAdapter().setBackgroundMode( SWT.INHERIT_DEFAULT );
    context.getAdapter().setBackground( context.getScrollable().getBackground() );
    horizontalBar = createFlatScrollBar( context, SWT.HORIZONTAL );
    verticalBar = createFlatScrollBar( context, SWT.VERTICAL );
    horizontalBar.addSelectionListener( createHorizontalSelectionListener( context ) );
    verticalBar.addSelectionListener( createVerticalSelectionListener( context ) );
    context.getAdapter().addDisposeListener( createWatchDog( context, horizontalBar, verticalBar ) );
    cornerOverlay = createCornerOverlay( context.getAdapter() );
    return create( context, horizontalBar, verticalBar );
  }

  private static Label createCornerOverlay( Composite parent ) {
    Label result = new Label( parent, SWT.NONE );
    result.setBackground( parent.getBackground() );
    result.moveAbove( null );
    return result;
  }

  @Override
  public ScrollBar getVerticalBarAdapter() {
    if( verticalAdapter == null ) {
      verticalAdapter = createFlatScrollBarAdapter( verticalBar );
    }
    return verticalAdapter;
  }

  @Override
  public ScrollBar getHorizontalBarAdapter() {
    if( horizontalAdapter == null ) {
      horizontalAdapter = createFlatScrollBarAdapter( horizontalBar );
    }
    return horizontalAdapter;
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

  @Override
  public void setBackgroundColor( Color backgroundColor ) {
    horizontalBar.setBackground( backgroundColor );
    verticalBar.setBackground( backgroundColor );
    cornerOverlay.setBackground( backgroundColor );
    verticalBar.getParent().setBackground( backgroundColor );
  }

  @Override
  public Color getBackgroundColor() {
    return horizontalBar.getBackground();
  }

  Label getCornerOverlay(){
    return cornerOverlay;
  }

  private FlatScrollBar createFlatScrollBar( AdaptionContext<T> context, int direction  ) {
    FlatScrollBar result = new FlatScrollBar( context.getAdapter(), direction );
    result.setBackground( context.getScrollable().getBackground() );
    return result;
  }

  private ScrollBar createFlatScrollBarAdapter( FlatScrollBar scrollBar ) {
    Class<? extends Widget> adapterClass = reflectionUtil.defineWidgetClass( SCROLL_BAR_ADAPTER_CLASS );
    Widget widget = reflectionUtil.newInstance( adapterClass );
    reflectionUtil.setField( widget, "display", Display.getCurrent() );
    reflectionUtil.setField( widget, "scrollBar", scrollBar );
    return ( ScrollBar )widget;
  }
}