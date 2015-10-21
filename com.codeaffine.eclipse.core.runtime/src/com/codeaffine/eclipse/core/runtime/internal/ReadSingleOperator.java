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