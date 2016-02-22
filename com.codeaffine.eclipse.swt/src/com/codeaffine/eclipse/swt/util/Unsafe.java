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
package com.codeaffine.eclipse.swt.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

public class Unsafe {

  private Object unsafeInstance;
  private Method allocateInstance;
  private Method defineClass;

  public Unsafe() {
    try {
      unsafeInstance = getUnsafeInstance();
      allocateInstance = getAllocateInstanceMethod();
      defineClass = getDefineClassMethod();
    } catch( Exception unresolvable ) {
      throw new IllegalStateException( unresolvable );
    }
  }

  public <T> T newInstance( Class<T> type ) {
    try {
      return type.cast( allocateInstance.invoke( unsafeInstance, type ) );
    } catch( IllegalAccessException iae ) {
      throw new RuntimeException( iae );
    } catch( InvocationTargetException ite ) {
      throw toRuntimeException( ite );
    }
  }

  public Class<?> defineClass(
    String name, byte[] bytes, int offset, int length, ClassLoader loader, ProtectionDomain domain )
  {
    Class<?> result = loadFromClassLoader( name, loader );
    if( result == null ) {
      result = defineClassUnsafe( name, bytes, offset, length, loader, domain );
    }
    return result;
  }

  private static Object getUnsafeInstance() throws Exception {
    Field unsafeField = getUnsafeClass().getDeclaredField( "theUnsafe" );
    unsafeField.setAccessible( true );
    return unsafeField.get( null );
  }

  private static Method getAllocateInstanceMethod() throws Exception {
    return getUnsafeClass().getMethod( "allocateInstance", Class.class );
  }

  private static Method getDefineClassMethod() throws Exception {
    return getUnsafeClass().getMethod(
      "defineClass", String.class, byte[].class, int.class, int.class, ClassLoader.class, ProtectionDomain.class );
  }

  private static Class<?> getUnsafeClass() throws ClassNotFoundException {
    return Unsafe.class.getClassLoader().loadClass( "sun.misc.Unsafe" );
  }

  private static Class<?> loadFromClassLoader( String name, ClassLoader loader ) {
    try {
      return loader.loadClass( name );
    } catch( SecurityException ignored ) {
      return null;
    } catch( ClassNotFoundException ignored ) {
      return null;
    }
  }

  private Class<?> defineClassUnsafe(
    String name, byte[] bytes, int offset, int length, ClassLoader loader, ProtectionDomain domain )
  {
    try {
      return ( Class<?> )defineClass.invoke(
        unsafeInstance, name, bytes, Integer.valueOf( offset), Integer.valueOf( length ), loader, domain );
    } catch( RuntimeException rte ) {
      throw rte;
    } catch( InvocationTargetException ite ) {
      throw toRuntimeException( ite );
    } catch( IllegalAccessException iae ) {
      throw new RuntimeException( iae );
    }
  }

  private static RuntimeException toRuntimeException( InvocationTargetException e ) {
    if( e.getCause() instanceof RuntimeException ) {
      return ( RuntimeException )e.getCause();
    }
    return new RuntimeException( e.getCause() );
  }
}