package com.codeaffine.eclipse.core.runtime;

import java.util.ArrayList;
import java.util.List;

public class TestExtensionConfigurator implements ExecutableExtensionConfigurator<TestExtension> {

  private final List<TestExtension> extensions;

  public TestExtensionConfigurator() {
    this.extensions = new ArrayList<TestExtension>();
  }

  @Override
  public void configure( TestExtension testExecutableExtension, Extension extension ) {
    extensions.add( testExecutableExtension );
    testExecutableExtension.setId( extension.getAttribute( "id" ) );
  }

  public List<TestExtension> getExtensions() {
    return extensions;
  }
}