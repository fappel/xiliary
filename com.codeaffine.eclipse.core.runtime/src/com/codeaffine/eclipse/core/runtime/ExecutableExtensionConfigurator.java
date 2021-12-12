/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.core.runtime;


public interface ExecutableExtensionConfigurator<T> {

  public class DefaultConfigurator<T> implements ExecutableExtensionConfigurator<T> {
    @Override
    public void configure( T executableExtension, Extension extension ) {}
  }

  void configure( T executableExtension, Extension extension );
}