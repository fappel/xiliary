package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;

import com.codeaffine.eclipse.core.runtime.RegistryAdapter.ExecutableExtensionConfiguration;
import com.codeaffine.eclipse.core.runtime.RegistryAdapter.ForEachSelector;
import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionsOperator;

public class ExecutableExtensionsCreator<T>
  implements ExecutableExtensionConfiguration<T>, ForEachSelector<T>
{

  private final CreateExecutableExtensionsOperator<T> operator;

  ExecutableExtensionsCreator( CreateExecutableExtensionsOperator<T> operator ) {
    this.operator = operator;
  }

  @Override
  public ExecutableExtensionsCreator<T> withConfiguration(
    ExecutableExtensionConfigurator<T> configurator )
  {
    verifyNotNull( configurator, "configurator" );

    operator.setConfigurator( configurator );
    return this;
  }

  @Override
  public ExecutableExtensionsCreator<T> withExceptionHandler(
    ExtensionExceptionHandler exceptionHandler )
  {
    verifyNotNull( exceptionHandler, "exceptionHandler" );

    operator.setExceptionHandler( exceptionHandler );
    return this;
  }

  @Override
  public ExecutableExtensionsCreator<T> withTypeAttribute( String typeAttribute ) {
    verifyNotNull( typeAttribute, "typeAttribute" );

    operator.setTypeAttribute( typeAttribute );
    return this;
                                                              }

  @Override
  public MultiProcessor<T> forEachContributionTo( String extensionPointId ) {
    verifyNotNull( extensionPointId, "extensionPointId" );

    operator.setExtensionPointId( extensionPointId );
    return new MultiProcessor<T>( operator );
  }
}