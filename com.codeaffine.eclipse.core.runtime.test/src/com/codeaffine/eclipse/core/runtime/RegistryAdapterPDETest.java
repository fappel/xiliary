package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.core.runtime.Platform.getExtensionRegistry;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.internal.OperatorFactory;

public class RegistryAdapterPDETest {

  private static final String EXTENSION_POINT_ID = "extension.point.id";

  private RegistryAdapter adapter;
  private OperatorFactory factory;

  @Before
  public void setUp() {
    factory = spy( new OperatorFactory( getExtensionRegistry() ) );
    adapter = new RegistryAdapter( factory );
  }

  @Test
  public void readExtension() {
    ReadSingleProcessor<Extension> actual = adapter.readExtension( EXTENSION_POINT_ID );

    assertThat( actual ).isNotNull();
    verify( factory ).newReadExtensionOperator( EXTENSION_POINT_ID );
  }

  @Test( expected = IllegalArgumentException.class )
  public void readExtensionWithNullAsExtensionPointId() {
    adapter.readExtension( null );
  }

  @Test
  public void readExtensions() {
    ReadMultiProcessor<Extension> actual = adapter.readExtensions( EXTENSION_POINT_ID );

    assertThat( actual ).isNotNull();
    verify( factory ).newReadExtensionsOperator( EXTENSION_POINT_ID );
  }

  @Test( expected = IllegalArgumentException.class )
  public void readExtensionsWithNullAsExtensionPointId() {
    adapter.readExtensions( null );
  }

  @Test
  public void createExecutableExtension() {
    CreateSingleProcessor<Runnable> actual
      = adapter.createExecutableExtension( EXTENSION_POINT_ID, Runnable.class );

    assertThat( actual ).isNotNull();
    verify( factory ).newCreateExecutableExtensionOperator( EXTENSION_POINT_ID, Runnable.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void createExecutableExtensionWithNullAsExtensionPointId() {
    adapter.createExecutableExtension( null, Runnable.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void createExecutableExtensionWithNullAsExtensionType() {
    adapter.createExecutableExtension( EXTENSION_POINT_ID, null );
  }

  @Test
  public void createExecutableExtensions() {
    CreateMultiProcessor<Runnable> actual
      = adapter.createExecutableExtensions( EXTENSION_POINT_ID, Runnable.class );

    assertThat( actual ).isNotNull();
    verify( factory ).newCreateExecutableExtensionsOperator( EXTENSION_POINT_ID, Runnable.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void createExecutableExtensionsWithNullAsExtensionPointId() {
    adapter.createExecutableExtensions( null, Runnable.class );
  }

  @Test( expected = IllegalArgumentException.class )
  public void createExecutableExtensionsWithNullAsExtensionType() {
    adapter.createExecutableExtensions( EXTENSION_POINT_ID, null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void constructorWithNullAsRegistry() {
     new RegistryAdapter( ( IExtensionRegistry )null );
  }
}