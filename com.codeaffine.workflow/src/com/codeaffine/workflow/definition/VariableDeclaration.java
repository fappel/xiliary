/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.definition;

import static com.codeaffine.workflow.internal.ArgumentVerification.verifyCondition;
import static com.codeaffine.workflow.internal.ArgumentVerification.verifyNotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableDeclaration<T> {

  private static final Pattern VALID_JAVA_FIELD_NAME_PATTERN
    = Pattern.compile( "(^[a-zA-Z][a-zA-Z0-9_]*)|(^[_][a-zA-Z0-9_]+)" );

  private final Class<T> type;
  private final String name;

  public VariableDeclaration( String name, Class<T> type ) {
    verifyNotNull( type, "type" );
    verifyName( name );

    this.name = name;
    this.type = type;
  }

  public Class<T> getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + name.hashCode();
    result = prime * result + type.hashCode();
    return result;
  }

  @Override
  public boolean equals( Object obj ) {
    if( this == obj ) {
      return true;
    }
    if( obj == null ) {
      return false;
    }
    if( getClass() != obj.getClass() ) {
      return false;
    }
    @SuppressWarnings( "rawtypes" )
    VariableDeclaration other = ( VariableDeclaration )obj;
    if( !name.equals( other.name ) ) {
      return false;
    }
    if( !type.equals( other.type ) ) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "VariableDeclaration [type=" + type.getName() + ", attributeName=" + name + "]";
  }

  private static void verifyName( String name ) {
    verifyNotNull( name, "name" );
    Matcher matcher = VALID_JAVA_FIELD_NAME_PATTERN.matcher( name );
    verifyCondition(  matcher.matches(), String.format( "<%s> is not a valid variable name.", name ) );
  }
}