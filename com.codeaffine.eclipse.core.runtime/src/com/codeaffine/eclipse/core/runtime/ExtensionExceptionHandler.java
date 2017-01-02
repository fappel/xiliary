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
package com.codeaffine.eclipse.core.runtime;

import org.eclipse.core.runtime.CoreException;

public interface ExtensionExceptionHandler {

  public static ExtensionExceptionHandler DEFAULT_HANDLER = new ExtensionExceptionHandler() {
    @Override
    public void handle( CoreException cause ) throws ExtensionException {
      throw new ExtensionException( cause );
    }
  };

  void handle( CoreException cause );
}
