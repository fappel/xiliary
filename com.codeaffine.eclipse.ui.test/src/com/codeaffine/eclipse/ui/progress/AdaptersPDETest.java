package com.codeaffine.eclipse.ui.progress;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrown;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.test.util.lang.ThrowableCaptor.Actor;

public class AdaptersPDETest {

  @SuppressWarnings("rawtypes")
  private static final Class<Collection> ADAPTER_TYPE_1 = Collection.class;
  private static final Class<Runnable> ADAPTER_TYPE_2 = Runnable.class;

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
    Runnable actual = adapters.getAdapter( null, ADAPTER_TYPE_2 );

    assertThat( actual ).isNull();
  }

  @Test
  public void getAdapterWithInstanceOfAdaptableType() {
    Runnable expected = mock( ADAPTER_TYPE_2 );

    Runnable actual = adapters.getAdapter( expected, ADAPTER_TYPE_2 );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void getAdapterWithAdaptableImplementation() {
    Collection<Object> expected = new ArrayList<Object>();
    IAdaptable adaptable = stubAdaptable( expected, ADAPTER_TYPE_1 );

    Object actual = adapters.getAdapter( adaptable, ADAPTER_TYPE_1 );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void getAdapterWithAdaptableImplementationIfNoAdapterIsFound() {
    IAdaptable adaptable = stubAdaptable( null, ADAPTER_TYPE_1 );

    Object actual = adapters.getAdapter( adaptable, ADAPTER_TYPE_1 );

    assertThat( actual ).isNull();
  }

  @Test
  @SuppressWarnings("unchecked")
  public void getAdapterFromAdapterRegistry() {
    Collection<Object> expected = new ArrayList<Object>();
    registerAdapterFactory( stubAdapterFactory( expected ), Boolean.class );

    Collection<Object> actual = adapters.getAdapter( Boolean.TRUE, ADAPTER_TYPE_1 );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void getAdapterFromAdapterRegistryIfNoAdapterIsFound() {
    registerAdapterFactory( stubAdapterFactory( null ), Boolean.class );

    Collection<Object> actual = adapters.getAdapter( Boolean.TRUE, ADAPTER_TYPE_1 );

    assertThat( actual ).isNull();
  }

  @Test
  public void getAdapterFromAdapterRegistryWithNonMatchingAdapterType() {
    Collection<Object> expected = new ArrayList<Object>();
    registerAdapterFactory( stubAdapterFactory( expected ), Boolean.class );

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        adapters.getAdapter( Boolean.TRUE, ADAPTER_TYPE_2 );
      }
    } );

    assertThat( actual ).isInstanceOf( ClassCastException.class );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void getAdapterFromAdapterRegistryButEmptyAdaptableImplementation() {
    Collection<Object> expected = new ArrayList<Object>();
    IPath adaptable = stubAdaptable( null, IPath.class, ADAPTER_TYPE_1 );
    registerAdapterFactory( stubAdapterFactory( expected ), IPath.class );

    Collection<Object> actual = adapters.getAdapter( adaptable, ADAPTER_TYPE_1 );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  @SuppressWarnings("unchecked")
  public void getAdapterFromAdapterRegistryWithPlatformObject() {
    Collection<Object> expected = new ArrayList<Object>();
    UnadaptableObject unAdaptable = new UnadaptableObject();
    registerAdapterFactory( stubAdapterFactory( expected ), UnadaptableObject.class );

    Collection<Object> actual = adapters.getAdapter( unAdaptable, ADAPTER_TYPE_1 );

    assertThat( actual ).isNull();
  }

  @Test
  public void getAdapterWithAdaptableImplementationWithNonMatchingAdapterType() {
    Collection<Object> expected = new ArrayList<Object>();
    final IAdaptable adaptable = stubAdaptable( expected, ADAPTER_TYPE_2 );

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        adapters.getAdapter( adaptable, ADAPTER_TYPE_2 );
      }
    } );

    assertThat( actual ).isInstanceOf( ClassCastException.class );
  }

  @Test( expected = AssertionFailedException.class )
  public void getAdapterWithNullAdapterType() {
    adapters.getAdapter( mock( ADAPTER_TYPE_2 ), null );
  }

  private static IAdaptable stubAdaptable( Object expected, Class<?> adapterType  ) {
    IAdaptable result = mock( IAdaptable.class );
    when( ( Object )result.getAdapter( adapterType ) ).thenReturn( expected );
    return result;
  }

  private static <T> T stubAdaptable( Object expected, Class<T> type, Class<?> adapterType ) {
    T result = mock( type, withSettings().extraInterfaces( IAdaptable.class ) );
    when( ( Object )( ( IAdaptable )result ).getAdapter( adapterType ) ).thenReturn( expected );
    return result;
  }

  @SuppressWarnings("unchecked")
  private static IAdapterFactory stubAdapterFactory( Collection<Object> expected ) {
    IAdapterFactory result = mock( IAdapterFactory.class );
    when( result.getAdapter( anyObject(), any( Class.class ) ) ).thenReturn( expected );
    when( result.getAdapterList() ).thenReturn( new Class[] { ADAPTER_TYPE_1, ADAPTER_TYPE_2 } );
    return result;
  }

  private void registerAdapterFactory( IAdapterFactory adapterFactory, Class<?> type ) {
    Platform.getAdapterManager().registerAdapters( adapterFactory, type  );
    this.adapterFactory = adapterFactory;
  }
}