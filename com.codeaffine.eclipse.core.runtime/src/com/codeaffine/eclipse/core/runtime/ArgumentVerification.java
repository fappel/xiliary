package com.codeaffine.eclipse.core.runtime;

import static java.lang.String.format;

class ArgumentVerification {

  static final String NOT_NULL_PATTERN = "Argument '%s' must not be null.";

  static void verifyNotNull( Object argument, String argumentName ) {
    if( argument == null ) {
      throw new IllegalArgumentException( format( NOT_NULL_PATTERN, argumentName ) );
    }
  }
}