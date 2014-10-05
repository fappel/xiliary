package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.DefaultContributionPredicate.ALL;
import static com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler.DEFAULT_HANDLER;
import static com.codeaffine.eclipse.core.runtime.RegistryAdapter.DEFAULT_TYPE_ATTRIBUTE;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.ContributionPredicate;
import com.codeaffine.eclipse.core.runtime.ExecutableExtensionConfigurator;
import com.codeaffine.eclipse.core.runtime.ExecutableExtensionConfigurator.DefaultConfigurator;
import com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler;
import com.codeaffine.eclipse.core.runtime.internal.ContributionElementLoop.ConfigurationElementHandler;
import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionsOperator;

class CreateMultiOperator<T> implements CreateExecutableExtensionsOperator<T> {

  private final ExtensionCreatorFactory<T> factory;
  private final ContributionElementLoop loop;
  private final Class<T> extensionType;

  private ExtensionExceptionHandler exceptionHandler;
  private ExecutableExtensionConfigurator<T> configurator;
  private ContributionPredicate predicate;
  private String extensionPointId;
  private String typeAttribute;

  CreateMultiOperator( IExtensionRegistry registry, Class<T> extensionType ) {
    this.configurator = new DefaultConfigurator<T>();
    this.typeAttribute = DEFAULT_TYPE_ATTRIBUTE;
    this.exceptionHandler = DEFAULT_HANDLER;
    this.predicate = ALL;
    this.factory = new ExtensionCreatorFactory<T>();
    this.loop = new ContributionElementLoop( registry );
    this.extensionType = extensionType;
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
  public void setPredicate( ContributionPredicate predicate ) {
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
  public Collection<T> create() {
    final Collection<T> result = new ArrayList<T>();
    loop.forEach( extensionPointId, predicate, new ConfigurationElementHandler() {
      @Override
      public void handle( IConfigurationElement element ) {
        collectExtension( result, element );
      }
    } );
    return result;
  }

  private void collectExtension( Collection<T> extensions, IConfigurationElement element ) {
    T extension = newCreator( element ).create();
    if( extension != null ) {
      extensions.add( extension );
    }
  }

  private ExtensionCreator<T> newCreator( IConfigurationElement element ) {
    return factory.create( element, extensionType, exceptionHandler, configurator, typeAttribute );
  }
}