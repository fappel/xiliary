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
package com.codeaffine.workflow.internal;

import static java.lang.String.format;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.WorkflowContext;
import com.codeaffine.workflow.definition.VariableDeclaration;

public class NodeLoaderImpl implements NodeLoader {

  static final String ERROR_DEFAULT_CONSTRUCTOR_MISSING = "Node types must provide a default constructor [%s].";
  static final String ERROR_CHECKED_EXCEPTION = "Node type constructors must not throw checked exceptions [%s].";
  static final String ERROR_OUTSIDE_LOAD = "Call to lookup outside of workflow node creation is not allowed.";

  public static ThreadLocal<WorkflowContext> lookupContext = new ThreadLocal<WorkflowContext>();

  public static <T> T lookup( VariableDeclaration<T> declaration ) {
    checkOnLoad();

    T result = null;
    if( lookupContext.get().hasVariableDefinition( declaration  ) ) {
      Object value = lookupContext.get().getVariableValue( declaration );
      result = declaration.getType().cast( value );
    }
    return result;
  }

  @Override
  public <T> T load( Class<T> toLoad, WorkflowContext context ) {
    try {
      Constructor<T> constructor = toLoad.getDeclaredConstructor();
      constructor.setAccessible( true );
      return doLoad( context, constructor );
    } catch( RuntimeException re ) {
      throw re;
    } catch( NoSuchMethodException nsme ) {
      throw new IllegalArgumentException( format( ERROR_DEFAULT_CONSTRUCTOR_MISSING, toLoad.getName() ), nsme );
    } catch( InvocationTargetException ite ) {
      if( ite.getCause() instanceof RuntimeException ) {
        throw ( RuntimeException )ite.getCause();
      }
      throw new IllegalArgumentException( format( ERROR_CHECKED_EXCEPTION, toLoad.getName() ), ite.getCause() );
    } catch( IllegalAccessException shouldNotHappen ) {
      throw new IllegalArgumentException( shouldNotHappen );
    } catch( InstantiationException shouldNotHappen ) {
      throw new IllegalArgumentException( shouldNotHappen );
    }
  }

  private static <T> T doLoad( WorkflowContext context, Constructor<T> constructor )
    throws InstantiationException, IllegalAccessException, InvocationTargetException
  {
    lookupContext.set( context );
    try {
      return constructor.newInstance();
    } finally {
      lookupContext.set( null );
    }
  }

  private static void checkOnLoad() {
    if( lookupContext.get() == null ) {
      throw new IllegalStateException( ERROR_OUTSIDE_LOAD );
    }
  }
}