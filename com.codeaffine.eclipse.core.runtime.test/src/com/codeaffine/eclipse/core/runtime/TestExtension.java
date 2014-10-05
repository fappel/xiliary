package com.codeaffine.eclipse.core.runtime;


public class TestExtension {

  public static final String EXTENSION_POINT
    = "com.codeaffine.eclipse.core.runtime.registryAdapterTestContribution";

  private String id;

  public String getId() {
    return id;
  }

  public void setId( String id ) {
    this.id = id;
  }
}