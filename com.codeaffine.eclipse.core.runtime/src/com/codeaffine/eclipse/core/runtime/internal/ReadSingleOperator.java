/**
 * Copyright (c) 2014 - 2022 Frank Appel
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

import java.util.function.Predicate;

import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionOperator;

class ReadSingleOperator implements ReadExtensionOperator<Extension> {

  private final ContributionFinder finder;
  private final String extensionPointId;

  private Predicate<Extension> predicate;

  ReadSingleOperator( IExtensionRegistry registry, String extensionPointId  ) {
    this.extensionPointId = extensionPointId;
    this.finder = new ContributionFinder( registry );
    this.predicate = alwaysTrue();
  }

  @Override
  public void setPredicate( Predicate<Extension> predicate ) {
    finder.find( extensionPointId, predicate );
    this.predicate = predicate;
  }

  @Override
  public Extension create() {
    return new Extension( finder.find( extensionPointId, predicate ) );
  }
}