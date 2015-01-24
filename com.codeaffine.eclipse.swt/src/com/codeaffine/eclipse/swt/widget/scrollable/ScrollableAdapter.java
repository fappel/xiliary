package com.codeaffine.eclipse.swt.widget.scrollable;

import java.awt.IllegalComponentStateException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;

class ScrollableAdapter<T extends Scrollable> extends Composite {

  private final LayoutFactory<T> layoutFactory;
  private final T scrollable;

  ScrollableAdapter(
    Composite parent, Platform platform, ScrollableFactory<T> factory, LayoutMapping<T> ... mappings )
  {
    super( parent, SWT.NONE );
    scrollable = createScrollable( factory );
    layoutFactory = createLayoutFactory( platform, mappings );
    super.setLayout( createLayout( this, scrollable, layoutFactory ) );
  }

  @Override
  public void setLayout( Layout layout ) {
    throw new UnsupportedOperationException( getClass().getName() + " does not allow to change layout." );
  }

  public void setIncrementButtonLength( int length ) {
    layoutFactory.setIncrementButtonLength( length );
  }

  public int getIncrementButtonLength() {
    return layoutFactory.getIncrementButtonLength();
  }

  public void setIncrementColor( Color color ) {
    layoutFactory.setIncrementColor( color );
  }

  public Color getIncrementColor() {
    return layoutFactory.getIncrementColor();
  }

  public void setPageIncrementColor( Color color ) {
    layoutFactory.setPageIncrementColor( color );
  }

  public Color getPageIncrementColor() {
    return layoutFactory.getPageIncrementColor();
  }

  public void setThumbColor( Color color ) {
    layoutFactory.setThumbColor( color );
  }

  public Color getThumbColor() {
    return layoutFactory.getThumbColor();
  }

  T getScrollable() {
    return scrollable;
  }

  private T createScrollable( ScrollableFactory<T> factory ) {
    T result = factory.create( this );
    checkParent( factory, result );
    return result;
  }

  private void checkParent( ScrollableFactory<T> factory, T result ) {
    if( result.getParent() != this ) {
      String factoryName = factory.getClass().getName();
      String message = "'" + factoryName + "' creates an instance that is not a child of parent composite parameter.";
      throw new IllegalComponentStateException( message );
    }
  }

  private Layout createLayout( ScrollableAdapter<T> adapter, T scrollable, LayoutFactory<T> layoutFactory ) {
    return layoutFactory.create( adapter, scrollable );
  }

  private static <T extends Scrollable> LayoutFactory<T> createLayoutFactory(
    Platform platform, LayoutMapping<T>[] mappings  )
  {
    LayoutFactory<T> result = new NativeLayoutFactory<T>();
    for( LayoutMapping<T> layoutMapping : mappings ) {
      if( platform.matchesOneOf( layoutMapping.getPlatformTypes() ) ) {
        result = layoutMapping.getLayoutFactory();
      }
    }
    return result;
  }
}