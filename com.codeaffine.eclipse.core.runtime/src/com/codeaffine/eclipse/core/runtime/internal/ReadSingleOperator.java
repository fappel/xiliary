package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.Predicates.alwaysTrue;

import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.Predicate;
import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionOperator;

class ReadSingleOperator implements ReadExtensionOperator<Extension> {

  private final ContributionFinder finder;
  private final String extensionPointId;

  private Predicate predicate;

  ReadSingleOperator( IExtensionRegistry registry, String extensionPointId  ) {
    this.extensionPointId = extensionPointId;
    this.finder = new ContributionFinder( registry );
    this.predicate = alwaysTrue();
  }

  @Override
  public void setPredicate( Predicate predicate ) {
    finder.find( extensionPointId, predicate );
    this.predicate = predicate;
  }

  @Override
  public Extension create() {
    return new Extension( finder.find( extensionPointId, predicate ) );
  }
}