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

import static com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler.DEFAULT_HANDLER;
import static com.codeaffine.eclipse.core.runtime.Predicates.alwaysTrue;
import static com.codeaffine.eclipse.core.runtime.RegistryAdapter.DEFAULT_TYPE_ATTRIBUTE;

import java.util.function.Predicate;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.ExecutableExtensionConfigurator;
import com.codeaffine.eclipse.core.runtime.ExecutableExtensionConfigurator.DefaultConfigurator;
import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler;
import com.codeaffine.eclipse.core.runtime.FindException;
import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionOperator;

class CreateSingleOperator<T> implements CreateExecutableExtensionOperator<T> {

  private final ExtensionCreatorFactory<T> factory;
  private final ContributionFinder finder;
  private final String extensionPointId;
  private final Class<T> extensionType;

  private ExtensionExceptionHandler exceptionHandler;
  private ExecutableExtensionConfigurator<T> configurator;
  private Predicate<Extension> predicate;
  private String typeAttribute;

  CreateSingleOperator( IExtensionRegistry registry, String extensionPointId , Class<T> extensionType ) {
    this.extensionPointId = extensionPointId;
    this.configurator = new DefaultConfigurator<T>();
    this.typeAttribute = DEFAULT_TYPE_ATTRIBUTE;
    this.exceptionHandler = DEFAULT_HANDLER;
    this.predicate = alwaysTrue();
    this.extensionType = extensionType;
    this.factory = new ExtensionCreatorFactory<T>();
    this.finder = new ContributionFinder( registry );
  }

  @Override
  public void setTypeAttribute( String typeAttribute ) {
    this.typeAttribute = typeAttribute;
  }

  @Override
  public void setPredicate( Predicate<Extension> predicate ) throws FindException {
    finder.find( extensionPointId, predicate );
    this.predicate = predicate;
  }

  @Override
  public void setConfigurator( ExecutableExtensionConfigurator<T> configurator ) {
    this.configurator = configurator;
  }

  @Override
  public void setExceptionHandler( ExtensionExceptionHandler exceptionHandler ) {
    this.exceptionHandler = exceptionHandler;
  }

  @Override
  public T create() throws FindException {
    return newCreator().create();
  }

  private ExtensionCreator<T> newCreator() {
    IConfigurationElement element = finder.find( extensionPointId, predicate );
    return factory.create( element, extensionType, exceptionHandler, configurator, typeAttribute );
  }
}