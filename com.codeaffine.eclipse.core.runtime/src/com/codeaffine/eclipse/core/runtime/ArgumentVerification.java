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
package com.codeaffine.eclipse.core.runtime;

import static java.lang.String.format;

import java.util.Iterator;
import java.util.function.Predicate;

class ArgumentVerification {

  static final String NOT_NULL_PATTERN = "Argument '%s' must not be null.";
  static final String NO_NULL_ELEMENT_PATTERN = "Argument '%s' must contain null elements.";

  static <T> T verifyNotNull( T argument, String argumentName ) {
    if( argument == null ) {
      throw new IllegalArgumentException( format( NOT_NULL_PATTERN, argumentName ) );
    }
    return argument;
  }

  static void verifyNoNullElement( Iterable<? extends Predicate<?>> iterable, String argumentName ) {
    Iterator<? extends Predicate<?>> iterator = iterable.iterator();
    while( iterator.hasNext() ) {
      if( iterator.next() == null ) {
        throw new IllegalArgumentException( format( NO_NULL_ELEMENT_PATTERN, argumentName ) );
      }
    }
  }
}