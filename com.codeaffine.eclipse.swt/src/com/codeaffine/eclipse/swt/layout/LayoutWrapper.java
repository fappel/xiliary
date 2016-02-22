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
package com.codeaffine.eclipse.swt.layout;

import static java.lang.Boolean.valueOf;
import static java.lang.Integer.valueOf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

public class LayoutWrapper extends Layout {

  private final Layout layout;

  @FunctionalInterface
  interface Producer<T> {
    T produce() throws Exception;
  }

  public LayoutWrapper( Layout layout ) {
    this.layout = layout;
  }

  @Override
  public Point computeSize( Composite composite, int wHint, int hHint, boolean flushCache ) {
    return runWithExceptionHandling( () -> doComputeSize( composite, wHint, hHint, flushCache ) );
  }

  @Override
  public void layout( Composite composite, boolean flushCache ) {
    runWithExceptionHandling( () -> {
      doLayout( composite, flushCache );
      return null;
    } );
  }

  private Point doComputeSize( Composite composite, int wHint, int hHint, boolean flushCache ) throws Exception {
    Method method = getDeclaredMethod( "computeSize", Composite.class, int.class, int.class, boolean.class );
    return ( Point )method.invoke( layout, composite, valueOf( wHint ), valueOf( hHint ), valueOf( flushCache ) );
  }

  private void doLayout( Composite composite, boolean flushCache ) throws Exception {
    Method method = getDeclaredMethod( "layout", Composite.class, boolean.class );
    method.invoke( layout, composite, valueOf( flushCache ) );
  }

  private static Method getDeclaredMethod( String name, Class<?>... parameterTypes ) throws Exception {
    Method result = Layout.class.getDeclaredMethod( name, parameterTypes );
    result.setAccessible( true );
    return result;
  }

  private static <T> T runWithExceptionHandling( Producer<T> producer ) {
    try {
      return producer.produce();
    } catch( InvocationTargetException ite ) {
      if( ite.getCause() instanceof RuntimeException ) {
        throw ( RuntimeException )ite.getCause();
      }
      throw new IllegalStateException( ite.getCause() );
    } catch( Exception shouldNotHappen ) {
      throw new IllegalStateException( shouldNotHappen );
    }
  }
}