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