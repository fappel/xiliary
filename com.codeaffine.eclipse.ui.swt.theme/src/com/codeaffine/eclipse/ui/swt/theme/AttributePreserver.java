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

import static com.codeaffine.eclipse.swt.util.ArgumentVerification.verifyNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.swt.widgets.Scrollable;

class AttributePreserver {

  private static final String ATTRIBUTES_MAP = AttributePreserver.class.getName() + "#ATTRIBUTES";

  private final Scrollable scrollable;

  AttributePreserver( Scrollable scrollable ) {
    this.scrollable = verifyNotNull( scrollable, "scrollable" );
  }

  <T> void put( AttributeKey<T> key, T value ) {
    verifyNotNull( key, "key" );
    verifyNotNull( value, "value" );

    getAttributes().put( key, value );
  }

  <T> Optional<T> get( AttributeKey<T> key ) {
    verifyNotNull( key, "key" );

    if( !getAttributes().containsKey( key ) ) {
      return Optional.empty();
    }
    return Optional.of( key.type.cast( getAttributes().get( key ) ) );
  }

  @SuppressWarnings("unchecked")
  private Map<AttributeKey<?>, Object> getAttributes() {
    if( scrollable.getData( ATTRIBUTES_MAP ) == null ) {
      Map<AttributeKey<?>, Object> attributes = new HashMap<>();
      scrollable.setData( ATTRIBUTES_MAP, attributes );
    }
    return ( Map<AttributeKey<?>, Object> )scrollable.getData( ATTRIBUTES_MAP );
  }
}