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

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.Extension;

class ContributionElementLoop {

  private final IExtensionRegistry registry;

  ContributionElementLoop( IExtensionRegistry registry ) {
    this.registry = registry;
  }

  void forEach( String extensionPointId, Predicate<Extension> predicate, Consumer<IConfigurationElement> handler ) {
    Stream.of( registry.getConfigurationElementsFor( extensionPointId ) )
      .filter( element -> predicate.test( new Extension( element ) ) )
      .forEach( element -> handler.accept( element ) );
  }
}