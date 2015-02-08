package com.codeaffine.eclipse.swt.widget.scrollable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.google.gson.internal.UnsafeAllocator;

class ControlReflectionUtil {

  private final UnsafeAllocator unsafeAllocator;

  interface Operator<T> {
    T execute() throws Exception;
  }

  ControlReflectionUtil() {
    unsafeAllocator = UnsafeAllocator.create();
  }

  <T extends Control> T newInstance( final Class<T> type ) {
    return execute( new Operator<T>() {
      @Override
      public T execute() throws Exception {
        return createNewIewInstance( type );
      }
    } );
  }

  void invoke( final Object receiver, final String methodName ) {
    execute( new Operator<Object>() {
      @Override
      public Object execute() throws Exception {
        return invokeMethod( receiver, methodName );
      }
    } );
  }

  void setField( final Object receiver, final String fieldName, final Object value  ) {
    execute( new Operator<Object>() {
      @Override
      public Object execute() throws Exception {
        return setFieldValue( receiver, fieldName, value );
      }
    } );
  }

  private <T extends Control> T createNewIewInstance( Class<T> type ) throws Exception {
    return unsafeAllocator.newInstance( type );
  }

  private static Object invokeMethod( Object receiver, String methodName ) throws Exception {
    Method method = getMethod( methodName );
    method.setAccessible( true );
    method.invoke( receiver );
    return null;
  }

  private static Method getMethod( String methodName ) throws NoSuchMethodException {
    try {
      return Control.class.getDeclaredMethod( methodName );
    } catch( NoSuchMethodException e ) {
      return Widget.class.getDeclaredMethod( methodName );
    }
  }

  private static Object setFieldValue( Object receiver, String fieldName, Object value ) throws Exception {
    Field field = getField( fieldName );
    field.setAccessible( true );
    field.set( receiver, value );
    return null;
  }

  private static Field getField( String fieldName ) throws NoSuchFieldException {
    try {
      return Control.class.getDeclaredField( fieldName );
    } catch( NoSuchFieldException nsfe ) {
      return Widget.class.getDeclaredField( fieldName );
    }
  }

  private static <T> T execute( Operator<T> operator ) {
    try {
      return operator.execute();
    } catch( RuntimeException rte ) {
      throw rte;
    } catch( InvocationTargetException ite ) {
      if( ite.getCause() instanceof RuntimeException ) {
        throw ( RuntimeException )ite.getCause();
      }
      throw new RuntimeException( ite.getCause() );
    } catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }
}