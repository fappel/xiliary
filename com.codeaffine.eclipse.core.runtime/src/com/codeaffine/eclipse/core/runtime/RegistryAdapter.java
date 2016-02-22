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
package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.codeaffine.eclipse.core.runtime.internal.OperatorFactory;

public class RegistryAdapter {

  public static final String DEFAULT_TYPE_ATTRIBUTE = "class";

  private final OperatorFactory factory;

  interface ExecutableExtensionConfiguration<T> {
    ExecutableExtensionConfiguration<T> withConfiguration( ExecutableExtensionConfigurator<T> configurator );
    ExecutableExtensionConfiguration<T> withExceptionHandler( ExtensionExceptionHandler exceptionHandler );
    ExecutableExtensionConfiguration<T> withTypeAttribute( String typeAttribute );
  }

  public RegistryAdapter() {
    this( Platform.getExtensionRegistry() );
  }

  public RegistryAdapter( IExtensionRegistry registry ) {
    this( new OperatorFactory( verifyNotNull( registry, "registry" ) ) );
  }

  RegistryAdapter( OperatorFactory operatorFactory ) {
    factory = operatorFactory;
  }

  public ReadSingleProcessor<Extension> readExtension( String extensionPointId ) {
    verifyNotNull( extensionPointId, "extensionPointId" );

    return new ReadSingleProcessor<Extension>( factory.newReadExtensionOperator( extensionPointId ) );
  }

  public ReadMultiProcessor<Extension> readExtensions( String extensionPointId ) {
    verifyNotNull( extensionPointId, "extensionPointId" );

    return new ReadMultiProcessor<Extension>( factory.newReadExtensionsOperator( extensionPointId ) );
  }

  public <T> CreateSingleProcessor<T> createExecutableExtension( String extensionPointId, Class<T> extensionType ) {
    verifyNotNull( extensionPointId, "extensionPointId" );
    verifyNotNull( extensionType, "extensionType" );

    return new CreateSingleProcessor<T>(
      factory.newCreateExecutableExtensionOperator( extensionPointId, extensionType )
    );
  }

  public <T> CreateMultiProcessor<T> createExecutableExtensions( String extensionPointId, Class<T> extensionType ) {
    verifyNotNull( extensionPointId, "extensionPointId" );
    verifyNotNull( extensionType, "extensionType" );

    return new CreateMultiProcessor<T>(
      factory.newCreateExecutableExtensionsOperator( extensionPointId, extensionType )
    );
  }
}