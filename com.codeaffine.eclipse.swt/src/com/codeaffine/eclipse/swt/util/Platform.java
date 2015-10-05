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