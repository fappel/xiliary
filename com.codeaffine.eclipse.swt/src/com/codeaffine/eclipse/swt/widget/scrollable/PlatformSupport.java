package com.codeaffine.eclipse.swt.widget.scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType;

class PlatformSupport {

  private final PlatformType[] supportedTypes;
  private final Platform platform;

  PlatformSupport( PlatformType ... supportedTypes ) {
    this.supportedTypes = supportedTypes;
    this.platform = new Platform();
  }

  boolean isGranted() {
    return platform.matchesOneOf( supportedTypes );
  }

  public PlatformType[] getSupportedTypes() {
    return supportedTypes;
  }
}