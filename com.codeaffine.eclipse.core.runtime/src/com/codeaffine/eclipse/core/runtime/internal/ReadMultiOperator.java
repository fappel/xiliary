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

import static com.codeaffine.eclipse.core.runtime.Predicates.alwaysTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionsOperator;

class ReadMultiOperator implements ReadExtensionsOperator<Extension> {

  private final ContributionElementLoop loop;
  private final String extensionPointId;

  private Predicate<Extension> predicate;

  ReadMultiOperator( IExtensionRegistry registry, String extensionPointId  ) {
    this.extensionPointId = extensionPointId;
    this.loop = new ContributionElementLoop( registry );
    this.predicate = alwaysTrue();
  }

  @Override
  public void setPredicate( Predicate<Extension> predicate ) {
    this.predicate = predicate;
  }

  @Override
  public Collection<Extension> create() {
    Collection<Extension> result = new ArrayList<Extension>();
    loop.forEach( extensionPointId, predicate, element -> result.add( new Extension( element ) ) );
    return result;
  }
}