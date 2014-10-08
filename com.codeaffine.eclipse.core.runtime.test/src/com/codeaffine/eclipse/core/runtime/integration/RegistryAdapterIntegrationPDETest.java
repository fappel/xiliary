package com.codeaffine.eclipse.core.runtime.integration;

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static com.codeaffine.eclipse.core.runtime.TestExtension.EXTENSION_POINT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler;
import com.codeaffine.eclipse.core.runtime.RegistryAdapter;
import com.codeaffine.eclipse.core.runtime.TestExtension;
import com.codeaffine.eclipse.core.runtime.TestExtensionConfigurator;

public class RegistryAdapterIntegrationPDETest {

  private RegistryAdapter adapter;

  @Before
  public void setUp() {
    adapter = new RegistryAdapter( Platform.getExtensionRegistry() );
  }

  @Test
  public void readExtension() {
    Extension actual
      = adapter.readExtension()
          .ofContributionTo( EXTENSION_POINT )
          .thatMatches( attribute( "id", "1" ) )
          .process();

    assertThat( actual.getAttribute( "id" ) ).isEqualTo( "1" );
  }

  @Test
  public void readExtensions() {
    Collection<Extension> actuals
      = adapter.readExtensions()
          .forEachContributionTo( EXTENSION_POINT )
          .thatMatches( attribute( "id", "1" ) )
          .process();

    assertThat( actuals ).hasSize( 1 );
    assertThat( actuals.iterator().next().getAttribute( "id" ) ).isEqualTo( "1" );
  }

  @Test
  public void createExecutableExtension() {
    TestExtension actual
      = adapter.createExecutableExtension( TestExtension.class )
          .withConfiguration( new TestExtensionConfigurator() )
          .ofContributionTo( EXTENSION_POINT )
          .thatMatches( attribute( "id", "1" ) )
          .process();

    assertThat( actual.getId() ).isEqualTo( "1" );
  }

  @Test
  public void createExecutableExtensions() {
    Collection<TestExtension> actuals
      = adapter.createExecutableExtensions( TestExtension.class )
          .withConfiguration( new TestExtensionConfigurator() )
          .forEachContributionTo( EXTENSION_POINT )
          .thatMatches( attribute( "id", "1" ) )
          .process();

    assertThat( actuals ).hasSize( 1 );
    assertThat( actuals.iterator().next().getId() ).isEqualTo( "1" );
  }

  @Test
  public void createExecutableExtensionsWithExceptionHandler() {
    ExtensionExceptionHandler exceptionHandler = mockExceptionHandler();
    Collection<TestExtension> actuals
      = adapter.createExecutableExtensions( TestExtension.class )
          .withExceptionHandler( exceptionHandler )
          .withTypeAttribute( "undefined" )
          .forEachContributionTo( EXTENSION_POINT )
          .process();

    assertThat( actuals ).isEmpty();
    verify( exceptionHandler, times( 2 ) ).handle( any( CoreException.class ) );
  }

  private static ExtensionExceptionHandler mockExceptionHandler() {
    return mock( ExtensionExceptionHandler.class );
  }
}