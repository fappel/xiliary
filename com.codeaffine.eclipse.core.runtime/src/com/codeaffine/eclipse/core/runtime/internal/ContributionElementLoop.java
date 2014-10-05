package com.codeaffine.eclipse.core.runtime.internal;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.ContributionPredicate;
import com.codeaffine.eclipse.core.runtime.Extension;

class ContributionElementLoop {

  private final IExtensionRegistry registry;

  public static interface ConfigurationElementHandler {
    void handle( IConfigurationElement element );
  }

  ContributionElementLoop( IExtensionRegistry registry ) {
    this.registry = registry;
  }

  void forEach(
    String extensionPointId, ContributionPredicate predicate, ConfigurationElementHandler handler )
  {
    for( IConfigurationElement elem : registry.getConfigurationElementsFor( extensionPointId ) ) {
new Exception( "TODO: exceptionHandler apply" ).printStackTrace();
      if( predicate.apply( new Extension( elem ) ) ) {
        handler.handle( elem );
      }
    }
  }
}