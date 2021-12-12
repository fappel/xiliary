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
package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionOperator;

@SuppressWarnings("unchecked")
public class CreateSingleProcessorTest {

  private CreateExecutableExtensionOperator<Runnable> operator;
  private CreateSingleProcessor<Runnable> processor;

  @Before
  public void setUp() {
    operator = mock( CreateExecutableExtensionOperator.class );
    processor = new CreateSingleProcessor<Runnable>( operator );
  }

  @Test
  public void withConfiguration() {
    ExecutableExtensionConfigurator<Runnable> configurator = mock( ExecutableExtensionConfigurator.class );

    CreateSingleProcessor<Runnable> actual = processor.withConfiguration( configurator );

    assertThat( actual ).isSameAs( processor );
    verify( operator ).setConfigurator( configurator );
  }

  @Test( expected = IllegalArgumentException.class )
  public void withConfigurationWithNullAsConfigurator() {
    processor.withConfiguration( null );
  }

  @Test
  public void withExceptionHandler() {
    ExtensionExceptionHandler handler
      = mock( ExtensionExceptionHandler.class );

    CreateSingleProcessor<Runnable> actual = processor.withExceptionHandler( handler );

    assertThat( actual ).isSameAs( processor );
    verify( operator ).setExceptionHandler( handler );

  }

  @Test( expected = IllegalArgumentException.class )
  public void withExceptionHandlerWithNullAsExceptionHandler() {
    processor.withExceptionHandler( null );
  }

  @Test
  public void withTypeAttribute() {
    String attributeType = "type";

    CreateSingleProcessor<Runnable> actual = processor.withTypeAttribute( attributeType );

    assertThat( actual ).isSameAs( processor );
    verify( operator ).setTypeAttribute( attributeType );

  }

  @Test( expected = IllegalArgumentException.class )
  public void withTypeAttributeWithNullAsTypeAttribute() {
    processor.withTypeAttribute( null );
  }
}