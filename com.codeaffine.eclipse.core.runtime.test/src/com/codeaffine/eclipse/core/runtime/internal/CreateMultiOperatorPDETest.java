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
package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static com.codeaffine.eclipse.core.runtime.TestExtension.EXTENSION_POINT;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.ExtensionException;
import com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler;
import com.codeaffine.eclipse.core.runtime.TestExtension;
import com.codeaffine.eclipse.core.runtime.TestExtensionConfigurator;

public class CreateMultiOperatorPDETest {

  private CreateMultiOperator<TestExtension> operator;

  @Before
  public void setUp() {
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    operator = new CreateMultiOperator<TestExtension>( registry, EXTENSION_POINT, TestExtension.class );
  }

  @Test
  public void create() {
    Collection<TestExtension> actuals = operator.create();

    assertThat( actuals ).hasSize( 2 );
  }

  @Test
  public void createWithConfigurator() {
    operator.setConfigurator( new TestExtensionConfigurator() );
    operator.setPredicate( attribute( "id", "1" ) );

    Collection<TestExtension> actuals = operator.create();

    assertThat( actuals.iterator().next().getId() ).isEqualTo( "1" );
  }

  @Test
  public void createOnProblemWithDefaultExceptionHandler() {
    operator.setTypeAttribute( "unknown" );

    Throwable actual = thrownBy( () -> operator.create() );

    assertThat( actual )
      .isInstanceOf( ExtensionException.class )
      .hasCauseInstanceOf( CoreException.class );
  }

  @Test
  public void createOnProblemWithCustomExceptionHandler() {
    ExtensionExceptionHandler exceptionHandler = mockExceptionHandler();
    operator.setExceptionHandler( exceptionHandler );
    operator.setTypeAttribute( "unknown" );

    Collection<TestExtension> actuals = operator.create();

    assertThat( actuals ).isEmpty();
    verify( exceptionHandler, times( 2 ) ).handle( any( CoreException.class ) );
  }

  @Test
  public void createWithPredication() {
    operator.setPredicate( attribute( "id", "1" ) );

    Collection<TestExtension> actuals = operator.create();

    assertThat( actuals ).hasSize( 1 );
  }

  private static ExtensionExceptionHandler mockExceptionHandler() {
    return mock( ExtensionExceptionHandler.class );
  }
}