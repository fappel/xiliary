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
package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.internal.ExtensionCreatorHelper.EXTENSION_ID;
import static com.codeaffine.eclipse.core.runtime.internal.ExtensionCreatorHelper.newExtensionCreator;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.ExtensionException;
import com.codeaffine.eclipse.core.runtime.TestExceptionHandler;
import com.codeaffine.eclipse.core.runtime.TestExtension;
import com.codeaffine.eclipse.core.runtime.TestExtensionConfigurator;

public class ExtensionCreatorPDETest {

  @Test
  public void create() {
    ExtensionCreator<TestExtension> creator = newExtensionCreator();

    TestExtension actual = creator.create();

    assertThat( actual ).isNotNull();
  }

  @Test
  public void createWithNoneDefaultTypeAttribute() {
    ExtensionCreator<TestExtension> creator = newExtensionCreator( "type" );

    TestExtension actual = creator.create();

    assertThat( actual ).isNotNull();
  }

  @Test( expected = ExtensionException.class )
  public void createWithUndefinedTypeAttribute() {
    ExtensionCreator<TestExtension> creator = newExtensionCreator( "undefined" );

    creator.create();
  }

  @Test
  public void createWithConfiguration() {
    TestExtensionConfigurator configurator = new TestExtensionConfigurator();
    ExtensionCreator<TestExtension> creator = newExtensionCreator( configurator );

    TestExtension extension = creator.create();

    assertThat( extension.getId() ).isEqualTo( EXTENSION_ID );
    assertThat( configurator.getExtensions() )
      .contains( extension )
      .hasSize( 1 );
  }

  @Test
  public void createExtensionWithExceptionHandler() {
    TestExceptionHandler exceptionHandler = new TestExceptionHandler();
    ExtensionCreator<TestExtension> creator = newExtensionCreator( "undefined", exceptionHandler );

    TestExtension extension = creator.create();

    assertThat( exceptionHandler.getExceptions() ).hasSize( 1 );
    assertThat( extension ).isNull();
  }
}