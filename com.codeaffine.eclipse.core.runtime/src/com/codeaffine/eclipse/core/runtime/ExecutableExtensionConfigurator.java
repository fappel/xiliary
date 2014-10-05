package com.codeaffine.eclipse.core.runtime;


public interface ExecutableExtensionConfigurator<T> {

  public class DefaultConfigurator<T> implements ExecutableExtensionConfigurator<T> {
    @Override
    public void configure( T executableExtension, Extension extension ) {}
  }

  void configure( T executableExtension, Extension extension );
}