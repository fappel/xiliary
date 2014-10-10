package com.codeaffine.eclipse.core.runtime;

import static java.lang.String.format;

import java.util.Iterator;

class ArgumentVerification {

  static final String NOT_NULL_PATTERN = "Argument '%s' must not be null.";
  static final String NO_NULL_ELEMENT_PATTERN = "Argument '%s' must contain null elements.";

  static <T> T verifyNotNull( T argument, String argumentName ) {
    if( argument == null ) {
      throw new IllegalArgumentException( format( NOT_NULL_PATTERN, argumentName ) );
    }
    return argument;
  }

  static void verifyNoNullElement( Iterable<? extends Predicate> iterable, String argumentName ) {
    Iterator<? extends Predicate> iterator = iterable.iterator();
    while( iterator.hasNext() ) {
      if( iterator.next() == null ) {
        throw new IllegalArgumentException( format( NO_NULL_ELEMENT_PATTERN, argumentName ) );
      }
    }
  }
}