package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;

class ScrollableAdapterFactoryHelper {

  public static <S extends Scrollable, A extends Scrollable & Adapter<S>> A adapt( S table, Class<A> type ) {
    ScrollableAdapterFactory factory = new ScrollableAdapterFactory();
    A result = factory.create( table, type );
    ScrollbarStyle style = ( ScrollbarStyle )result;
    style.setThumbColor( Display.getCurrent().getSystemColor( SWT.COLOR_RED ) );
    return result;
  }
}