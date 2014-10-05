package com.codeaffine.eclipse.core.runtime.internal;

import org.eclipse.core.runtime.IConfigurationElement;

import com.codeaffine.eclipse.core.runtime.ExecutableExtensionConfigurator;
import com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler;

class ExtensionCreatorFactory<T> {

  ExtensionCreator<T> create( IConfigurationElement element,
                              Class<T> extensionType,
                              ExtensionExceptionHandler exceptionHandler,
                              ExecutableExtensionConfigurator<T> configurator,
                              String typeAttribute )
  {
    return new ExtensionCreator<T>(
      element, extensionType, exceptionHandler, configurator, typeAttribute
    );
  }
}