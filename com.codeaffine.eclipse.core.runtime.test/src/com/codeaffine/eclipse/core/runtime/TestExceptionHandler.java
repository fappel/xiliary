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

import org.eclipse.core.runtime.CoreException;

public class TestExceptionHandler implements ExtensionExceptionHandler {

  private final ArrayList<CoreException> exceptions;

  public TestExceptionHandler() {
    this.exceptions = new ArrayList<CoreException>();
  }

  @Override
  public void handle( CoreException creationException ) {
    exceptions.add( creationException );
  }


  public List<CoreException> getExceptions() {
    return exceptions;
  }
}