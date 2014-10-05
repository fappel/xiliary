package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;

import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.internal.OperatorFactory;

public class RegistryAdapter {

  public static final String DEFAULT_TYPE_ATTRIBUTE = "class";

  private final OperatorFactory factory;

  interface SingleSelector<T> {
    SingleProcessor<T> ofContributionTo( String extensionPointId );
  }

  interface ForEachSelector<T> {
    MultiProcessor<T> forEachContributionTo( String extensionPointId );
  }

  interface ExecutableExtensionConfiguration<T> {
    ExecutableExtensionConfiguration<T> withConfiguration( ExecutableExtensionConfigurator<T> configurator );
    ExecutableExtensionConfiguration<T> withExceptionHandler( ExtensionExceptionHandler exceptionHandler );
    ExecutableExtensionConfiguration<T> withTypeAttribute( String typeAttribute);
  }

  public RegistryAdapter( IExtensionRegistry registry ) {
    verifyNotNull( registry, "registry" );

    this.factory = new OperatorFactory( registry );
  }

  public ExtensionReader readExtension() {
    return new ExtensionReader( factory.newReadExtensionOperator() );
  }

  public ExtensionsReader readExtensions() {
    return new ExtensionsReader( factory.newReadExtensionsOperator() );
  }

  public <T> ExecutableExtensionCreator<T> createExecutableExtension( Class<T> extensionType ) {
    verifyNotNull( extensionType, "extensionType" );

    return new ExecutableExtensionCreator<T>(
      factory.newCreateExecutableExtensionOperator( extensionType )
    );
  }

  public <T> ExecutableExtensionsCreator<T> createExecutableExtensions( Class<T> extensionType ) {
    verifyNotNull( extensionType, "extensionType" );

    return new ExecutableExtensionsCreator<T>(
      factory.newCreateExecutableExtensionsOperator( extensionType )
    );
  }
}