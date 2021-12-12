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


public class TestExtension {

  public static final String EXTENSION_POINT
    = "com.codeaffine.eclipse.core.runtime.registryAdapterTestContribution";

  private String id;

  public String getId() {
    return id;
  }

  public void setId( String id ) {
    this.id = id;
  }
}