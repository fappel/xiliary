package com.codeaffine.eclipse.swt.util;

import static com.codeaffine.eclipse.swt.testhelper.TestResources.CLASS_NAME;
import static com.codeaffine.eclipse.swt.testhelper.TestResources.LOCATION;
import static com.codeaffine.eclipse.swt.testhelper.TestResources.PROTECTED_CLASS_NAME;
import static com.codeaffine.eclipse.swt.testhelper.TestResources.PROTECTED_LOCATION;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.security.ProtectionDomain;

import org.eclipse.swt.widgets.Widget;
import org.junit.Before;
import org.junit.Test;

public class UnsafeTest {

  private ResourceLoader resourceLoader;
  private Unsafe unsafe;

  @Before
  public void setUp() {
    resourceLoader = new ResourceLoader();
    unsafe = new Unsafe();
  }

  @Test
  public void defineClass() {
    byte[] bytes = resourceLoader.load( LOCATION );

    Class<?> actual = unsafe.defineClass( CLASS_NAME, bytes, 0, bytes.length, getLoader(), getDomain() );

    assertThat( actual.getName() ).isEqualTo( CLASS_NAME );
  }

  @Test
  public void defineProtectedClass() {
    ensureSignerInfoInCurrentClassLoader();
    byte[] bytes = resourceLoader.load( PROTECTED_LOCATION );

    Class<?> actual = unsafe.defineClass( PROTECTED_CLASS_NAME, bytes, 0, bytes.length, getLoader(), getDomain() );

    assertThat( actual.getName() ).isEqualTo( PROTECTED_CLASS_NAME );
  }

  @Test
  public void defineClassTwice() {
    byte[] bytes = resourceLoader.load( LOCATION );

    Class<?> first = unsafe.defineClass( CLASS_NAME, bytes, 0, bytes.length, getLoader(), getDomain() );
    Class<?> second = unsafe.defineClass( CLASS_NAME, bytes, 0, bytes.length, getLoader(), getDomain() );

    assertThat( first ).isSameAs( second );
  }

  @Test
  public void defineClassThatDoesNotExist() {
    byte[] bytes = resourceLoader.load( LOCATION );

    Throwable actual = thrownBy(
      () ->  unsafe.defineClass( "unknown", bytes, 0, bytes.length, getLoader(), getDomain() ) );

    assertThat( actual ).hasRootCauseInstanceOf( NoClassDefFoundError.class );
  }

  @Test
  public void newInstance() {
    UnsafeNewInstanceType actual = unsafe.newInstance( UnsafeNewInstanceType.class );

    assertThat( actual ).isInstanceOf( UnsafeNewInstanceType.class );
  }

  private ProtectionDomain getDomain() {
    return getClass().getProtectionDomain();
  }

  private ClassLoader getLoader() {
    return getClass().getClassLoader();
  }

  private static void ensureSignerInfoInCurrentClassLoader() {
    Widget.class.getName();
  }
}