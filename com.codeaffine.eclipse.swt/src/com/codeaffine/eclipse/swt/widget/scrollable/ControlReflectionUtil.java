package com.codeaffine.eclipse.swt.widget.scrollable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import com.codeaffine.eclipse.swt.util.ResourceLoader;
import com.codeaffine.eclipse.swt.util.Unsafe;

class ControlReflectionUtil {

  private final Unsafe unsafe;

  interface Operator<T> {
    T execute() throws Exception;
  }

  ControlReflectionUtil() {
    unsafe = new Unsafe();
  }

  public Class<? extends Widget> defineWidgetClass( final String name ) {
    return execute( new Operator<Class<? extends Widget>>() {
      @Override
      public Class<? extends Widget> execute() throws Exception {
        return defineClass( name );
      }
    } );
  }

  <T extends Widget> T newInstance( final Class<T> type ) {
    return execute( new Operator<T>() {
      @Override
      public T execute() throws Exception {
        return createNewIewInstance( type );
      }
    } );
  }

  void invoke( final Widget receiver, final String methodName ) {
    execute( new Operator<Object>() {
      @Override
      public Object execute() throws Exception {
        return invokeMethod( receiver, methodName );
      }
    } );
  }

  void setField( final Widget receiver, final String fieldName, final Object value  ) {
    execute( new Operator<Object>() {
      @Override
      public Object execute() throws Exception {
        return setFieldValue( receiver, fieldName, value );
      }
    } );
  }

  @SuppressWarnings("unchecked")
  private Class<? extends Widget> defineClass( final String name ) {
    String path = name.replaceAll( "\\.", "/" ) + ".class";
    byte[] bytes = new ResourceLoader().load( path );
    ClassLoader loader = getClass().getClassLoader();
    ProtectionDomain domain = getClass().getProtectionDomain();
    return ( Class<? extends Widget> )unsafe.defineClass( name, bytes, 0, bytes.length, loader, domain  );
  }

  private <T extends Widget> T createNewIewInstance( Class<T> type ) throws Exception {
    return unsafe.newInstance( type );
  }

  private static Object invokeMethod( Widget receiver, String methodName ) throws Exception {
    Method method = getMethod( receiver, methodName );
    method.setAccessible( true );
    method.invoke( receiver );
    return null;
  }

  private static Method getMethod( Widget receiver, String methodName ) throws NoSuchMethodException {
    try {
      return receiver.getClass().getDeclaredMethod( methodName );
    } catch( NoSuchMethodException noReceiverMethod ) {
      return Control.class.getDeclaredMethod( methodName );
    }
  }

  private static Object setFieldValue( Object receiver, String fieldName, Object value ) throws Exception {
    Field field = getField( receiver, fieldName );
    field.setAccessible( true );
    field.set( receiver, value );
    return null;
  }

  private static Field getField( Object receiver, String fieldName ) throws NoSuchFieldException {
    try {
      return receiver.getClass().getDeclaredField( fieldName );
    } catch( NoSuchFieldException noReceiverField ) {
      try {
        return Control.class.getDeclaredField( fieldName );
      } catch( NoSuchFieldException noControlField ) {
        return Widget.class.getDeclaredField( fieldName );
      }
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