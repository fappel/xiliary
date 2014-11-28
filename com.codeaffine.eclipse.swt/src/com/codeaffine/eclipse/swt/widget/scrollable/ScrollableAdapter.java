package com.codeaffine.eclipse.swt.widget.scrollable;

import java.awt.IllegalComponentStateException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;

class ScrollableAdapter<T extends Scrollable> extends Composite {

  private final T scrollable;

  ScrollableAdapter(
    Composite parent, Platform platform, ScrollableFactory<T> factory, LayoutMapping<T> ... mappings )
  {
    super( parent, SWT.NONE );
    this.scrollable = createScrollable( factory );
    super.setLayout( createLayout( this, scrollable, platform, mappings ) );
  }

  @Override
  public void setLayout( Layout layout ) {
    throw new UnsupportedOperationException( getClass().getName() + " does not allow to change layout." );
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

  private Layout createLayout(
    ScrollableAdapter<T> adapter, T scrollable, Platform platform, LayoutMapping<T>[] mappings )
  {
    return createLayoutFactory( platform, mappings ).create( adapter, scrollable );
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