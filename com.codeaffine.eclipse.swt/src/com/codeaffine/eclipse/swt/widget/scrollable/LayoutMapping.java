package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.Platform.PlatformType;

class LayoutMapping<T extends Scrollable> {

  private final LayoutFactory<T> layoutFactory;
  private final PlatformType[] platformTypes;

  LayoutMapping( LayoutFactory<T> layoutFactory, PlatformType ... platformTypes ) {
    this.layoutFactory = layoutFactory;
    this.platformTypes = platformTypes;
  }

  LayoutFactory<T> getLayoutFactory() {
    return layoutFactory;
  }

  PlatformType[] getPlatformTypes() {
    return platformTypes;
  }
}