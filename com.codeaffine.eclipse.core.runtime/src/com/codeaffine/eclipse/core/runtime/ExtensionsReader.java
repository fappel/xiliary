package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;

import com.codeaffine.eclipse.core.runtime.RegistryAdapter.ForEachSelector;
import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionsOperator;

public class ExtensionsReader implements ForEachSelector<Extension>{

  private final ReadExtensionsOperator<Extension> operator;

  ExtensionsReader( ReadExtensionsOperator<Extension> operator ) {
    this.operator = operator;
  }

  @Override
  public MultiProcessor<Extension> forEachContributionTo( String extensionPointId ) {
    verifyNotNull( extensionPointId, "extensionPointId" );

    operator.setExtensionPointId( extensionPointId );
    return new MultiProcessor<Extension>( operator );
  }
}