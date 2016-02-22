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
package com.codeaffine.eclipse.swt.util;

import org.eclipse.swt.SWT;

public class Platform {

  public enum PlatformType {
    COCOA( "cocoa" ), GTK( "gtk" ), WIN32( "win32" );

    final String value;

    PlatformType( String value ) {
      this.value = value;
    }
  }

  public boolean matches( PlatformType toCheck ) {
    return toCheck.value.equals( SWT.getPlatform() );
  }

  public boolean matchesOneOf( PlatformType ... platformTypes ) {
    boolean result = false;
    for( PlatformType platformType : platformTypes ) {
      result |= matches( platformType );
    }
    return result;
  }
}