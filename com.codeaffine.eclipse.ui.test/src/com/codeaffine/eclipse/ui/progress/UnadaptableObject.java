package com.codeaffine.eclipse.ui.progress;

import org.eclipse.core.runtime.PlatformObject;

class UnadaptableObject extends PlatformObject {
  @Override
  @SuppressWarnings({
    "unchecked",
    "rawtypes"
  })
  public Object getAdapter( Class adapter ) {
    return null;
  }
}