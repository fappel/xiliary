package com.codeaffine.eclipse.core.runtime;

import org.eclipse.core.runtime.CoreException;

public interface ExtensionExceptionHandler {

  public static ExtensionExceptionHandler DEFAULT_HANDLER = new ExtensionExceptionHandler() {
    @Override
    public void handle( CoreException cause ) throws ExtensionException {
      throw new ExtensionException( cause );
    }
  };

  void handle( CoreException cause );
}
