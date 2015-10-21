package com.codeaffine.eclipse.core.runtime.internal;

import java.util.Collection;
import java.util.function.Predicate;

import com.codeaffine.eclipse.core.runtime.ExecutableExtensionConfigurator;
import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler;

public interface Operator<T> {

  public interface ReadExtensionOperator<T> extends Operator<T> {
    T create();
  }

  public interface ReadExtensionsOperator<T> extends Operator<T> {
    Collection<T> create();
  }

  public interface ExecutableExtensionConfiguration<T> {
    void setConfigurator( ExecutableExtensionConfigurator<T> configurator );
    void setExceptionHandler( ExtensionExceptionHandler exceptionHandler );
    void setTypeAttribute( String typeAttribute );
  }

  public interface CreateExecutableExtensionOperator<T>
    extends ReadExtensionOperator<T>, ExecutableExtensionConfiguration<T> {}

  public interface CreateExecutableExtensionsOperator<T>
    extends ReadExtensionsOperator<T>, ExecutableExtensionConfiguration<T> {}

  void setPredicate( Predicate<Extension> predicate );
}