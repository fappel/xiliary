package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;

import com.codeaffine.eclipse.core.runtime.RegistryAdapter.ExecutableExtensionConfiguration;
import com.codeaffine.eclipse.core.runtime.RegistryAdapter.SingleSelector;
import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionOperator;

public class ExecutableExtensionCreator<T>
  implements ExecutableExtensionConfiguration<T>, SingleSelector<T>
{

  private final CreateExecutableExtensionOperator<T> operator;

  ExecutableExtensionCreator( CreateExecutableExtensionOperator<T> operator ) {
    this.operator = operator;
  }

  @Override
  public ExecutableExtensionCreator<T> withConfiguration(
    ExecutableExtensionConfigurator<T> configurator )
  {
    verifyNotNull( configurator, "configurator" );

    operator.setConfigurator( configurator );
    return this;
  }

  @Override
  public ExecutableExtensionCreator<T> withExceptionHandler(
    ExtensionExceptionHandler exceptionHandler )
  {
    verifyNotNull( exceptionHandler, "exceptionHandler" );

    operator.setExceptionHandler( exceptionHandler );
    return this;
  }

  @Override
  public ExecutableExtensionCreator<T> withTypeAttribute( String typeAttribute ) {
    verifyNotNull( typeAttribute, "typeAttribute" );

    operator.setTypeAttribute( typeAttribute );
    return this;
                                                            }

  @Override
  public SingleProcessor<T> ofContributionTo( String extensionPointId ) {
    verifyNotNull( extensionPointId, "extensionPointId" );

    operator.setExtensionPointId( extensionPointId );
    return new SingleProcessor<T>( operator );
  }
}