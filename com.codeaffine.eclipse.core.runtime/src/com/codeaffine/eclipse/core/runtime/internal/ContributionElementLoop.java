package com.codeaffine.eclipse.core.runtime.internal;

import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.Predicate;

class ContributionElementLoop {

  private final IExtensionRegistry registry;

  ContributionElementLoop( IExtensionRegistry registry ) {
    this.registry = registry;
  }

  void forEach( String extensionPointId, Predicate predicate, Consumer<IConfigurationElement> handler ) {
    Stream.of( registry.getConfigurationElementsFor( extensionPointId ) )
      .filter( element -> predicate.apply( new Extension( element ) ) )
      .forEach( element -> handler.accept( element ) );
  }
}