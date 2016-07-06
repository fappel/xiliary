/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.util.ArgumentVerification.verifyNotNull;

import org.eclipse.swt.graphics.Color;

import com.codeaffine.eclipse.swt.widget.scrollable.Demeanor;

class AttributeKey<T> {

  final String identifier;
  final Class<T> type;

  private AttributeKey( String identifier, Class<T> type ) {
    this.identifier = verifyNotNull( identifier, "identifier" );
    this.type = verifyNotNull( type, "type" );
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + identifier.hashCode();
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
    AttributeKey other = ( AttributeKey )obj;
    if( !identifier.equals( other.identifier ) ) {
      return false;
    }
    if( !type.equals( other.type ) ) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "StyleAttributeKey [identifier=" + identifier + ", type=" + type + "]";
  }

  static <T> AttributeKey<T> key( String identifier, Class<T> type ) {
    return new AttributeKey<>( identifier, type );
  }

  static AttributeKey<Color> colorKey( String identifier ) {
    return new AttributeKey<>( identifier, Color.class );
  }

  static AttributeKey<Demeanor> demeanorKey( String identifier ) {
    return key( identifier, Demeanor.class );
  }

  static AttributeKey<Integer> integerKey( String identifier ) {
    return new AttributeKey<>( identifier, Integer.class );
  }
}