/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
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
    adapter = new RegistryAdapter();
  }

  @Test
  public void readExtension() {
    Extension actual
      = adapter.readExtension( EXTENSION_POINT )
          .thatMatches( attribute( "id", "1" ) )
          .process();

    assertThat( actual.getAttribute( "id" ) ).isEqualTo( "1" );
  }

  @Test
  public void readExtensions() {
    Collection<Extension> actuals
      = adapter.readExtensions( EXTENSION_POINT )
          .thatMatches( attribute( "id", "1" ) )
          .process();

    assertThat( actuals ).hasSize( 1 );
    assertThat( actuals.iterator().next().getAttribute( "id" ) ).isEqualTo( "1" );
  }

  @Test
  public void createExecutableExtension() {
    TestExtension actual
      = adapter.createExecutableExtension( EXTENSION_POINT, TestExtension.class )
          .withConfiguration( new TestExtensionConfigurator() )
          .thatMatches( attribute( "id", "1" ) )
          .process();

    assertThat( actual.getId() ).isEqualTo( "1" );
  }

  @Test
  public void createExecutableExtensions() {
    Collection<TestExtension> actuals
      = adapter.createExecutableExtensions( EXTENSION_POINT, TestExtension.class )
          .withConfiguration( new TestExtensionConfigurator() )
          .thatMatches( attribute( "id", "1" ) )
          .process();

    assertThat( actuals ).hasSize( 1 );
    assertThat( actuals.iterator().next().getId() ).isEqualTo( "1" );
  }

  @Test
  public void createExecutableExtensionsWithExceptionHandler() {
    ExtensionExceptionHandler exceptionHandler = mockExceptionHandler();
    Collection<TestExtension> actuals
      = adapter.createExecutableExtensions( EXTENSION_POINT, TestExtension.class )
          .withExceptionHandler( exceptionHandler )
          .withTypeAttribute( "undefined" )
          .process();

    assertThat( actuals ).isEmpty();
    verify( exceptionHandler, times( 2 ) ).handle( any( CoreException.class ) );
  }

  private static ExtensionExceptionHandler mockExceptionHandler() {
    return mock( ExtensionExceptionHandler.class );
  }
}