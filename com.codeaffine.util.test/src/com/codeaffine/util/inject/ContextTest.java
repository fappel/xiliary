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
package com.codeaffine.util.inject;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static com.codeaffine.util.inject.Context.ABSTRACT_TYPE_CANNOT_BE_INSTANTIATED;
import static com.codeaffine.util.inject.Context.INTERFACE_CANNOT_BE_INSTANTIATED;
import static com.codeaffine.util.inject.Context.WRONG_CONSTRUCTOR_COUNT;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.util.Disposable;

public class ContextTest {

  private Context context;

  static class Pojo {

    final Runnable injected;

    Pojo( Runnable injected ) {
      this.injected = injected;
    }
  }

  static class TooManyConstructorPojo extends Pojo {

    TooManyConstructorPojo( Runnable injected ) {
      super( injected );
    }

    public TooManyConstructorPojo() {
      super( null );
    }
  }

  static abstract class AbstractPojo extends Pojo {

    AbstractPojo( Runnable injected ) {
      super( injected );
    }
  }

  static class PojoWithRuntimeException {

    PojoWithRuntimeException() {
      throw new RuntimeException( getClass().getName() );
    };
  }

  static class PojoWithCheckedException {

    PojoWithCheckedException() throws Exception {
      throw new Exception( getClass().getName() );
    };
  }

  private static class UnaccessiblePojo {
  }

  static class DisposablePojo implements Disposable {

    boolean disposed;

    @Override
    public void dispose() {
      disposed = true;
    }
  }

  @Before
  public void setUp() {
    context = new Context();
  }

  @Test
  public void set() {
    Runnable expected = mock( Runnable.class );

    context.set( Runnable.class, expected );
    Runnable actual = context.get( Runnable.class );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void setWithNullValue() {
    context.set( Runnable.class, null );

    Runnable actual = context.get( Runnable.class );

    assertThat( actual ).isNull();
  }

  @Test
  public void setWithNullKey() {
    Throwable actual = thrownBy( () -> context.set( null, mock( Runnable.class ) ) );

    assertThat( actual )
      .hasMessage( Context.ARGUMENT_KEY_MUST_NOT_BE_NULL )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  public void getWithNullKey() {
    Throwable actual = thrownBy( () -> context.get( null ) );

    assertThat( actual )
      .hasMessage( Context.ARGUMENT_KEY_MUST_NOT_BE_NULL )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  public void getIfValueNotSet() {
    Runnable actual = context.get( Runnable.class );

    assertThat( actual ).isNull();
  }

  @Test
  public void getOfContext() {
    Context actual = context.get( Context.class );

    assertThat( actual ).isSameAs( context );
  }

  @Test
  public void create() {
    Object actual = context.create( Object.class );

    assertThat( actual ).isNotNull();
  }

  @Test
  public void createWithInjection() {
    Runnable expected = mock( Runnable.class );
    context.set( Runnable.class, expected );

    Pojo actual = context.create( Pojo.class );

    assertThat( actual.injected ).isSameAs( expected );
  }

  @Test
  public void createWithInjectionIfParameterIsNotAvailable() {
    Pojo actual = context.create( Pojo.class );

    assertThat( actual.injected ).isNull();
  }

  @Test
  public void createWithMultipleConstructorPojo() {
    Throwable actual = thrownBy( () -> context.create( TooManyConstructorPojo.class ) );

    assertThat( actual )
      .hasMessage( format( WRONG_CONSTRUCTOR_COUNT, TooManyConstructorPojo.class.getName() ) )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  public void createInterfaceType() {
    Throwable actual = thrownBy( () -> context.create( Runnable.class ) );

    assertThat( actual )
      .hasMessage( format( INTERFACE_CANNOT_BE_INSTANTIATED, Runnable.class.getName() ) )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  public void createAbstractType() {
    Throwable actual = thrownBy( () -> context.create( AbstractPojo.class ) );

    assertThat( actual )
      .hasMessage( format( ABSTRACT_TYPE_CANNOT_BE_INSTANTIATED, AbstractPojo.class.getName() ) )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  public void createWithUnaccessiblePojo() {
    UnaccessiblePojo actual = context.create( UnaccessiblePojo.class );

    assertThat( actual ).isNotNull();
  }

  @Test
  public void createWithRuntimeExceptionThrownByPojoConstructor() {
    Throwable actual = thrownBy( () -> context.create( PojoWithRuntimeException.class ) );

    assertThat( actual )
      .hasMessage( PojoWithRuntimeException.class.getName() )
      .isInstanceOf( RuntimeException.class );
  }

  @Test
  public void createWithCheckedExceptionThrownByPojoConstructor() {
    Throwable actual = thrownBy( () -> context.create( PojoWithCheckedException.class ) );

    assertThat( actual )
      .hasCauseInstanceOf( Exception.class )
      .isInstanceOf( IllegalStateException.class );
    assertThat( actual.getCause() )
      .hasMessage( PojoWithCheckedException.class.getName() );
  }

  @Test
  public void dispose() {
    DisposablePojo value = new DisposablePojo();
    context.set( DisposablePojo.class, value );
    context.set( Object.class, new Object() );

    context.dispose();

    assertThat( value.disposed ).isTrue();
    assertThat( context.get( DisposablePojo.class ) ).isNull();
    assertThat( context.get( Object.class ) ).isNull();
  }
}