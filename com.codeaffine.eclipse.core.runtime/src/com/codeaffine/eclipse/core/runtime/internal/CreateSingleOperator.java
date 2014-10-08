package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler.DEFAULT_HANDLER;
import static com.codeaffine.eclipse.core.runtime.Predicates.alwaysTrue;
import static com.codeaffine.eclipse.core.runtime.RegistryAdapter.DEFAULT_TYPE_ATTRIBUTE;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.ExecutableExtensionConfigurator;
import com.codeaffine.eclipse.core.runtime.ExecutableExtensionConfigurator.DefaultConfigurator;
import com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler;
import com.codeaffine.eclipse.core.runtime.FindException;
import com.codeaffine.eclipse.core.runtime.Predicate;
import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionOperator;

class CreateSingleOperator<T> implements CreateExecutableExtensionOperator<T> {

  private final ExtensionCreatorFactory<T> factory;
  private final ContributionFinder finder;
  private final Class<T> extensionType;

  private ExtensionExceptionHandler exceptionHandler;
  private ExecutableExtensionConfigurator<T> configurator;
  private Predicate predicate;
  private String extensionPointId;
  private String typeAttribute;

  CreateSingleOperator( IExtensionRegistry registry, Class<T> extensionType ) {
    this.configurator = new DefaultConfigurator<T>();
    this.typeAttribute = DEFAULT_TYPE_ATTRIBUTE;
    this.exceptionHandler = DEFAULT_HANDLER;
    this.predicate = alwaysTrue();
    this.extensionType = extensionType;
    this.factory = new ExtensionCreatorFactory<T>();
    this.finder = new ContributionFinder( registry );
  }

  @Override
  public void setExtensionPointId( String extensionPointId ) {
    this.extensionPointId = extensionPointId;
  }

  @Override
  public void setTypeAttribute( String typeAttribute ) {
    this.typeAttribute = typeAttribute;
  }

  @Override
  public void setPredicate( Predicate predicate ) throws FindException {
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