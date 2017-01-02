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
package com.codeaffine.eclipse.swt.util;

import com.codeaffine.eclipse.swt.util.Platform.PlatformType;

public class PlatformSupport {

  private final PlatformType[] supportedTypes;
  private final Platform platform;

  public PlatformSupport( PlatformType ... supportedTypes ) {
    this.supportedTypes = supportedTypes;
    this.platform = new Platform();
  }

  public boolean isGranted() {
    return platform.matchesOneOf( supportedTypes );
  }

  public PlatformType[] getSupportedTypes() {
    return supportedTypes;
  }
}