package com.codeaffine.eclipse.core.runtime;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.runtime.IConfigurationElement;

public class Extension {

  private final IConfigurationElement element;

  public Extension( IConfigurationElement element ) {
    this.element = element;
  }

  public String getAttribute( String name ) {
    return element.getAttribute( name );
  }

  public String getAttribute( String name, String locale ) {
    return element.getAttribute( name, locale );
  }

  public Collection<String> getAttributeNames() {
    return Arrays.asList( element.getAttributeNames() );
  }

  public String getName() {
    return element.getName();
  }
}