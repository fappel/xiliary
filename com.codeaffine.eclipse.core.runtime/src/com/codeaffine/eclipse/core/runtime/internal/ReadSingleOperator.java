package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.DefaultContributionPredicate.ALL;

import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.ContributionPredicate;
import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionOperator;

class ReadSingleOperator implements ReadExtensionOperator<Extension> {

  private final ContributionFinder finder;

  private ContributionPredicate predicate;
  private String extensionPointId;

  ReadSingleOperator( IExtensionRegistry registry ) {
    finder = new ContributionFinder( registry );
    predicate = ALL;
  }

  @Override
  public void setExtensionPointId( String extensionPointId ) {
    this.extensionPointId = extensionPointId;
  }

  @Override
  public void setPredicate( ContributionPredicate predicate ) {
    finder.find( extensionPointId, predicate );
    this.predicate = predicate;
  }

  @Override
  public Extension create() {
    return new Extension( finder.find( extensionPointId, predicate ) );
  }
}