/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.createLayoutFactory;

import java.awt.IllegalComponentStateException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.util.Platform;
import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

public class ScrollableAdapter<T extends Scrollable> extends Composite implements ScrollbarStyle {

  private final LayoutFactory<T> layoutFactory;
  private final T scrollable;

  @SuppressWarnings("unchecked")
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

  @Override
  public void setIncrementButtonLength( int length ) {
    layoutFactory.setIncrementButtonLength( length );
  }

  @Override
  public int getIncrementButtonLength() {
    return layoutFactory.getIncrementButtonLength();
  }

  @Override
  public void setIncrementColor( Color color ) {
    layoutFactory.setIncrementColor( color );
  }

  @Override
  public Color getIncrementColor() {
    return layoutFactory.getIncrementColor();
  }

  @Override
  public void setPageIncrementColor( Color color ) {
    layoutFactory.setPageIncrementColor( color );
  }

  @Override
  public Color getPageIncrementColor() {
    return layoutFactory.getPageIncrementColor();
  }

  @Override
  public void setThumbColor( Color color ) {
    layoutFactory.setThumbColor( color );
  }

  @Override
  public Color getThumbColor() {
    return layoutFactory.getThumbColor();
  }

  @Override
  public void setBackgroundColor( Color color ) {
    layoutFactory.setBackgroundColor( color );
  }

  @Override
  public Color getBackgroundColor() {
    return layoutFactory.getBackgroundColor();
  }

  @Override
  public void setDemeanor( Demeanor demeanor ) {
    layoutFactory.setDemeanor( demeanor );
  }

  @Override
  public Demeanor getDemeanor() {
    return layoutFactory.getDemeanor();
  }

  public T getScrollable() {
    return scrollable;
  }

  private T createScrollable( ScrollableFactory<T> factory ) {
    T result = factory.create( this );
    new ScrollableAdapterFactory().markAdapted( result );
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
    return layoutFactory.create( new AdaptionContext<T>( adapter, new ScrollableControl<>( scrollable ) ) );
  }
}