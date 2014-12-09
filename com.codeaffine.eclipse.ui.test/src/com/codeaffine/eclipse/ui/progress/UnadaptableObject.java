package com.codeaffine.eclipse.ui.progress;

import org.eclipse.core.runtime.PlatformObject;

class UnadaptableObject extends PlatformObject {
  @Override
  public Object getAdapter( @SuppressWarnings("rawtypes") Class adapter ) {
    return null;
  }
}