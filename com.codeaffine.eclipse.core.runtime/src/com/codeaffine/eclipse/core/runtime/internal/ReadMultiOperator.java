package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.Predicates.alwaysTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.Predicate;
import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionsOperator;

class ReadMultiOperator implements ReadExtensionsOperator<Extension> {

  private final ContributionElementLoop loop;
  private final String extensionPointId;

  private Predicate predicate;

  ReadMultiOperator( IExtensionRegistry registry, String extensionPointId  ) {
    this.extensionPointId = extensionPointId;
    this.loop = new ContributionElementLoop( registry );
    this.predicate = alwaysTrue();
  }

  @Override
  public void setPredicate( Predicate predicate ) {
    this.predicate = predicate;
  }

  @Override
  public Collection<Extension> create() {
    Collection<Extension> result = new ArrayList<Extension>();
    loop.forEach( extensionPointId, predicate, element -> result.add( new Extension( element ) ) );
    return result;
  }
}