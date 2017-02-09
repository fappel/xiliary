/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.util.inject;

import static com.codeaffine.util.ArgumentVerification.verifyCondition;
import static com.codeaffine.util.ArgumentVerification.verifyNotNull;
import static java.lang.String.format;
import static java.lang.reflect.Modifier.isAbstract;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import com.codeaffine.util.Disposable;

public class Context implements Disposable {

  static final String TYPE_IS_NOT_ACCESSIBLE = "<%s> is not accessible and cannot be instantiated.";
  static final String INTERFACE_CANNOT_BE_INSTANTIATED = "<%s> is an interface and cannot be instantiated.";
  static final String ABSTRACT_TYPE_CANNOT_BE_INSTANTIATED = "<%s> is an abstract type and cannot be instantiated.";
  static final String ARGUMENT_KEY_MUST_NOT_BE_NULL = "Argument 'key' must not be null.";
  static final String WRONG_CONSTRUCTOR_COUNT
    = "<%s> has more than one constructor and cannot be managed by constructor injection.";
  static final String UNABLE_TO_CREATE_INSTANCE
    = "Unable to create instance of <%s> using constructor injection.";

  private final Map<Class<?>, Object> content;
  private final BiFunction<Constructor<?>, Context, Object[]> injectionParameterProvider;

  public Context() {
    this( ( constructor, context ) -> getInjectionParameters( constructor, context ) );
  }

  public Context( BiFunction<Constructor<?>, Context, Object[]> injectionParameterProvider ) {
    verifyNotNull( injectionParameterProvider, "injectionParameterProvider" );

    this.injectionParameterProvider = injectionParameterProvider;
    this.content = new HashMap<>();
    set( Context.class, this );
  }

  @Override
  public void dispose() {
    content.values().stream()
      .filter( value -> value instanceof Disposable && value != this )
      .map( value -> ( Disposable )value )
      .forEach( disposable -> disposable.dispose() );
    content.clear();
  }

  public <T> T get( Class<T> key ) {
    verifyNotNull( key, "key" );

    return key.cast( content.get( key ) );
  }

  public <T> void set( Class<T> key, T value ) {
    verifyNotNull( key, "key" );

    content.put( key, value );
  }

  public <T> T create( Class<T> type ) {
    verifyCondition( !type.isInterface(), INTERFACE_CANNOT_BE_INSTANTIATED, type.getName() );
    verifyCondition( !isAbstract( type.getModifiers() ), ABSTRACT_TYPE_CANNOT_BE_INSTANTIATED, type.getName() );
    verifyCondition( type.getDeclaredConstructors().length == 1, WRONG_CONSTRUCTOR_COUNT, type.getName() );

    return doCreate( type, type.getDeclaredConstructors()[ 0 ] );
  }

  private <T> T doCreate( Class<T> type, Constructor<?> constructor ) {
    try {
      constructor.setAccessible( true );
      return type.cast( constructor.newInstance( injectionParameterProvider.apply( constructor, this ) ) );
    } catch( RuntimeException rte ) {
      throw rte;
    } catch( InvocationTargetException ite ) {
      Throwable cause = ite.getCause();
      if( cause instanceof RuntimeException ) {
        throw ( RuntimeException )cause;
      }
      throw createIllegalStateExceptionWrapper( type, cause );
    } catch( IllegalAccessException e ) {
      throw new IllegalStateException( format( TYPE_IS_NOT_ACCESSIBLE, type.getName() ), e );
    } catch( InstantiationException e ) {
      throw createIllegalStateExceptionWrapper( type, e );
    }
  }

  private static Object[] getInjectionParameters( Constructor<?> constructor, Context context ) {
    Class<?>[] parameterTypes = constructor.getParameterTypes();
    return Stream.of( parameterTypes ).map( parameterType -> context.get( parameterType ) ).toArray();
  }

  private static <T> IllegalStateException createIllegalStateExceptionWrapper( Class<T> type, Throwable e ) {
    String message = format( UNABLE_TO_CREATE_INSTANCE, type.getName() );
    return new IllegalStateException( message, e );
  }
}