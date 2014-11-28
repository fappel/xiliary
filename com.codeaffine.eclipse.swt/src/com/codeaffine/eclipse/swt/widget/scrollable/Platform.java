package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;

class Platform {

  enum PlatformType {
    COCOA( "cocoa" ), GTK( "gtk" ), WIN32( "win32" );

    final String value;

    PlatformType( String value ) {
      this.value = value;
    }
  }

  boolean matches( PlatformType toCheck ) {
    return toCheck.value.equals( SWT.getPlatform() );
  }

  boolean matchesOneOf( PlatformType ... platformTypes ) {
    boolean result = false;
    for( PlatformType platformType : platformTypes ) {
      result |= matches( platformType );
    }
    return result;
  }
}