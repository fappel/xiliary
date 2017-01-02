/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
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