package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class RegistryAdapterPDETest {

  private RegistryAdapter adapter;

  @Before
  public void setUp() {
    adapter = new RegistryAdapter();
  }

  @Test
  public void readExtension() {
    ExtensionReader actual = adapter.readExtension();

    assertThat( actual ).isNotNull();
  }

  @Test
  public void readExtensions() {
    ExtensionsReader actual = adapter.readExtensions();

    assertThat( actual ).isNotNull();
  }

  @Test
  public void createExecutableExtension() {
    ExecutableExtensionCreator<Runnable> actual
      = adapter.createExecutableExtension( Runnable.class );

    assertThat( actual ).isNotNull();
  }

  @Test( expected = IllegalArgumentException.class )
  public void createExecutableExtensionWithNullAsExtensionType() {
    adapter.createExecutableExtension( null );
  }

  @Test
  public void createExecutableExtensions() {
    ExecutableExtensionsCreator<Runnable> actual
      = adapter.createExecutableExtensions( Runnable.class );

    assertThat( actual ).isNotNull();
  }

  @Test( expected = IllegalArgumentException.class )
  public void createExecutableExtensionsWithNullAsExtensionType() {
    adapter.createExecutableExtensions( null );
  }
}