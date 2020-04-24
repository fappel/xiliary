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
package com.codeaffine.eclipse.swt.util;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

public class Unsafe {

  private Object unsafeInstance;
  private Method allocateInstance;
  private Method defineClass;
  private Method privateLookupIn;

  public Unsafe() {
    try {
      unsafeInstance = getUnsafeInstance();
      allocateInstance = getAllocateInstanceMethod();
      try {
        defineClass = getUnsafeDefineClassMethod();
      } catch( NoSuchMethodException noSuchMethod ) {
        // These methods are part of JDK9+'s public API. However, getting them
        // via reflection ensures that the code still compiles on JDK8.
        defineClass = getLookupDefineClassMethod();
        privateLookupIn = getPrivateLookupInMethod();
      }
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
    String name,
    byte[] bytes,
    int offset,
    int length,
    ClassLoader loader,
    ProtectionDomain domain,
    Class<?> otherInPackage )
  {
    Class<?> result = loadFromClassLoader( name, loader );
    if( result == null ) {
      result = ( Class<?> )invokeDefineClass( name, bytes, offset, length, loader, domain, otherInPackage );
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

  private static Method getUnsafeDefineClassMethod() throws Exception {
    return getUnsafeClass().getMethod(
      "defineClass", String.class, byte[].class, int.class, int.class, ClassLoader.class, ProtectionDomain.class );
  }

  private static Class<?> getUnsafeClass() throws ClassNotFoundException {
    return Unsafe.class.getClassLoader().loadClass( "sun.misc.Unsafe" );
  }

  private static Method getLookupDefineClassMethod() throws Exception {
    return Lookup.class.getMethod( "defineClass", byte[].class );
  }

  private static Method getPrivateLookupInMethod() throws Exception {
    return MethodHandles.class.getMethod( "privateLookupIn", Class.class, Lookup.class );
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

  private Object invokeDefineClass(
    String name,
    byte[] bytes,
    int offset,
    int length,
    ClassLoader loader,
    ProtectionDomain domain,
    Class<?> otherInPackage )
  {
    try {
      if (privateLookupIn != null) {
        return defineClass.invoke( privateLookupIn.invoke( null, otherInPackage, MethodHandles.lookup() ), bytes );
      }
      return defineClass.invoke( unsafeInstance,
                                 name,
                                 bytes,
                                 Integer.valueOf( offset ),
                                 Integer.valueOf( length ),
                                 loader,
                                 domain );
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