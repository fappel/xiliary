package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.Predicates.alwaysTrue;

import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.Predicate;
import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionOperator;

class ReadSingleOperator implements ReadExtensionOperator<Extension> {

  private final ContributionFinder finder;

  private Predicate predicate;
  private String extensionPointId;

  ReadSingleOperator( IExtensionRegistry registry ) {
    finder = new ContributionFinder( registry );
    predicate = alwaysTrue();
  }

  @Override
  public void setExtensionPointId( String extensionPointId ) {
    this.extensionPointId = extensionPointId;
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