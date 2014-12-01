package com.codeaffine.eclipse.ui.progress;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.Platform;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.test.util.lang.ThrowableCaptor.Actor;

public class AdaptersPDETest {

  private IAdapterFactory adapterFactory;
  private Adapters adapters;

    @Before
  public void setUp() {
    adapters = new Adapters();
  }

  @After
  public void tearDown() {
    Platform.getAdapterManager().unregisterAdapters( adapterFactory );
  }

  @Test
  public void getAdapterWithNullAdaptable() {
    Runnable actual = adapters.getAdapter( null, Runnable.class );

    assertThat( actual ).isNull();
  }

  @Test
  public void getAdapterWithInstanceOfAdaptableType() {
    Runnable expected = mock( Runnable.class );

    Runnable actual = adapters.getAdapter( expected, Runnable.class );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void getAdapterWithAdaptableImplementation() {
    Collection<Object> expected = new ArrayList<Object>();
    IAdaptable adaptable = stubAdaptable( expected, Collection.class );

    Object actual = adapters.getAdapter( adaptable, Collection.class );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void getAdapterWithAdaptableImplementationIfNoAdapterIsFound() {
    IAdaptable adaptable = stubAdaptable( null, Collection.class );

    Object actual = adapters.getAdapter( adaptable, Collection.class );

    assertThat( actual ).isNull();
  }

  @Test
  @SuppressWarnings("unchecked")
  public void getAdapterFromAdapterRegistry() {
    Collection<Object> expected = new ArrayList<Object>();
    registerAdapterFactory( stubAdapterFactory( expected ), Boolean.class );

    Collection<Object> actual = adapters.getAdapter( Boolean.TRUE, Collection.class );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void getAdapterFromAdapterRegistryIfNoAdapterIsFound() {
    registerAdapterFactory( stubAdapterFactory( null ), Boolean.class );

    Collection<Object> actual = adapters.getAdapter( Boolean.TRUE, Collection.class );

    assertThat( actual ).isNull();
  }

  @Test
  public void getAdapterFromAdapterRegistryWithNonMatchingAdapterType() {
    Collection<Object> expected = new ArrayList<Object>();
    registerAdapterFactory( stubAdapterFactory( expected ), Boolean.class );

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        adapters.getAdapter( Boolean.TRUE, Runnable.class );
      }
    } );

    assertThat( actual ).isInstanceOf( ClassCastException.class );
  }

  @Test
  public void getAdapterWithAdaptableImplementationWithNonMatchingAdapterType() {
    Collection<Object> expected = new ArrayList<Object>();
    final IAdaptable adaptable = stubAdaptable( expected, Runnable.class );

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        adapters.getAdapter( adaptable, Runnable.class );
      }
    } );

    assertThat( actual ).isInstanceOf( ClassCastException.class );
  }

  @Test( expected = AssertionFailedException.class )
  public void getAdapterWithNullAdapterType() {
    adapters.getAdapter( mock( Runnable.class ), null );
  }

  private static IAdaptable stubAdaptable( Object expected, Class<?> adapterType  ) {
    IAdaptable result = mock( IAdaptable.class );
    when( result.getAdapter( adapterType ) ).thenReturn( expected );
    return result;
  }

  private static IAdapterFactory stubAdapterFactory( Collection<Object> expected ) {
    IAdapterFactory result = mock( IAdapterFactory.class );
    when( result.getAdapter( anyObject(), any( Class.class ) ) ).thenReturn( expected );
    when( result.getAdapterList() ).thenReturn( new Class[] { Collection.class, Runnable.class } );
    return result;
  }

  private void registerAdapterFactory( IAdapterFactory adapterFactory, Class<?> type ) {
    Platform.getAdapterManager().registerAdapters( adapterFactory, type  );
    this.adapterFactory = adapterFactory;
  }
}