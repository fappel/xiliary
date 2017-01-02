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

import java.util.ArrayList;
import java.util.List;

public class TestExtensionConfigurator implements ExecutableExtensionConfigurator<TestExtension> {

  private final List<TestExtension> extensions;

  public TestExtensionConfigurator() {
    this.extensions = new ArrayList<TestExtension>();
  }

  @Override
  public void configure( TestExtension testExecutableExtension, Extension extension ) {
    extensions.add( testExecutableExtension );
    testExecutableExtension.setId( extension.getAttribute( "id" ) );
  }

  public List<TestExtension> getExtensions() {
    return extensions;
  }
}