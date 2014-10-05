package com.codeaffine.eclipse.core.runtime.internal;

import org.eclipse.core.runtime.IExtensionRegistry;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionOperator;
import com.codeaffine.eclipse.core.runtime.internal.Operator.CreateExecutableExtensionsOperator;
import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionOperator;
import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionsOperator;

public class OperatorFactory {

  private final IExtensionRegistry registry;

  public OperatorFactory( IExtensionRegistry registry ) {
    this.registry = registry;
  }

  public ReadExtensionOperator<Extension> newReadExtensionOperator() {
    return new ReadSingleOperator( registry );
  }

  public ReadExtensionsOperator<Extension> newReadExtensionsOperator() {
    return new ReadMultiOperator( registry );
  }

  public <T> CreateExecutableExtensionOperator<T> newCreateExecutableExtensionOperator(
    Class<T> extensionType )
  {
    return new CreateSingleOperator<T>( registry, extensionType );
  }

  public <T> CreateExecutableExtensionsOperator<T> newCreateExecutableExtensionsOperator(
    Class<T> extensionType )
  {
    return new CreateMultiOperator<T>( registry, extensionType );
  }
}
