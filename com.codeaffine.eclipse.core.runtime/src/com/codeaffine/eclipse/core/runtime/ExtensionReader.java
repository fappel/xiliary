package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;

import com.codeaffine.eclipse.core.runtime.RegistryAdapter.SingleSelector;
import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionOperator;

public class ExtensionReader implements SingleSelector<Extension> {

  private final ReadExtensionOperator<Extension> operator;

  ExtensionReader( ReadExtensionOperator<Extension> operator ) {
    this.operator = operator;
  }

  @Override
  public SingleProcessor<Extension> ofContributionTo( String extensionPointId ) {
    verifyNotNull( extensionPointId, "extensionPointId" );

    operator.setExtensionPointId( extensionPointId );
    return new SingleProcessor<Extension>( operator );
  }
}