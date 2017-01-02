/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

public class Extension {

  private final IConfigurationElement element;

  public Extension( IConfigurationElement element ) {
    this.element = element;
  }

  public IConfigurationElement getConfigurationElement() {
    return element;
  }

  public String getAttribute( String name ) {
    return element.getAttribute( name );
  }

  public String getAttribute( String name, String locale ) {
    return element.getAttribute( name, locale );
  }

  public Collection<String> getAttributeNames() {
    return asList( element.getAttributeNames() );
  }

  public String getName() {
    return element.getName();
  }

  public <T> T createExecutableExtension( Class <T> type ) throws ExtensionException {
    return createExecutableExtension( "class", type );
  }

  public <T> T createExecutableExtension( String typeAttribute, Class <T> type ) throws ExtensionException {
    verifyNotNull( typeAttribute, "typeAttribute" );
    verifyNotNull( type, "type" );

    try {
      return type.cast( element.createExecutableExtension( typeAttribute ) );
    } catch( CoreException ce ) {
      throw new ExtensionException( ce );
    }
  }

  public String getValue() {
    return element.getValue();
  }

  public String getValue( String locale ) {
    return element.getValue( locale );
  }

  public Collection<Extension> getChildren() {
    List<Extension> result = new ArrayList<Extension>();
    for( IConfigurationElement child : element.getChildren() ) {
      result.add( new Extension( child ) );
    }
    return result;
  }

  public Collection<Extension> getChildren( String name ) {
    List<Extension> result = new ArrayList<Extension>();
    for( IConfigurationElement child : element.getChildren() ) {
      if( child.getName().equals( name ) ) {
        result.add( new Extension( child ) );
      }
    }
    return result;
  }
}