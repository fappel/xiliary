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

import static com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler.DEFAULT_HANDLER;
import static com.codeaffine.eclipse.core.runtime.TestExtension.EXTENSION_POINT;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import com.codeaffine.eclipse.core.runtime.ExecutableExtensionConfigurator;
import com.codeaffine.eclipse.core.runtime.ExecutableExtensionConfigurator.DefaultConfigurator;
import com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler;
import com.codeaffine.eclipse.core.runtime.TestExceptionHandler;
import com.codeaffine.eclipse.core.runtime.TestExtension;
import com.codeaffine.eclipse.core.runtime.TestExtensionConfigurator;

class ExtensionCreatorHelper {

  static final String EXTENSION_ID = "1";

  static ExtensionCreator<TestExtension> newExtensionCreator(
    String typeAttribute, TestExceptionHandler exceptionHandler )
  {
    return newExtensionCreator(
      exceptionHandler, new DefaultConfigurator<TestExtension>(), typeAttribute
    );
  }

  static ExtensionCreator<TestExtension> newExtensionCreator() {
    return newExtensionCreator( "class" );
  }

  static ExtensionCreator<TestExtension> newExtensionCreator( String typeAttribute ) {
    return newExtensionCreator(
      DEFAULT_HANDLER, new DefaultConfigurator<TestExtension>(), typeAttribute
    );
  }

  static ExtensionCreator<TestExtension> newExtensionCreator(
    TestExtensionConfigurator configurator )
  {
    return newExtensionCreator( DEFAULT_HANDLER, configurator, "class" );
  }

  private static ExtensionCreator<TestExtension> newExtensionCreator(
    ExtensionExceptionHandler exceptionHandler,
    ExecutableExtensionConfigurator<TestExtension> configurator,
    String typeAttribute )
  {
    IConfigurationElement element = findFirstTestContribution();
    return new ExtensionCreator<TestExtension>(
      element, TestExtension.class, exceptionHandler, configurator, typeAttribute
    );
  }

  private static IConfigurationElement findFirstTestContribution() {
    return Platform.getExtensionRegistry().getConfigurationElementsFor( EXTENSION_POINT )[ 0 ];
  }
}