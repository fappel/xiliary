/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.core.runtime.internal;

import org.eclipse.core.runtime.IConfigurationElement;

import com.codeaffine.eclipse.core.runtime.ExecutableExtensionConfigurator;
import com.codeaffine.eclipse.core.runtime.ExtensionExceptionHandler;

class ExtensionCreatorFactory<T> {

  ExtensionCreator<T> create( IConfigurationElement element,
                              Class<T> extensionType,
                              ExtensionExceptionHandler exceptionHandler,
                              ExecutableExtensionConfigurator<T> configurator,
                              String typeAttribute )
  {
    return new ExtensionCreator<T>(
      element, extensionType, exceptionHandler, configurator, typeAttribute
    );
  }
}