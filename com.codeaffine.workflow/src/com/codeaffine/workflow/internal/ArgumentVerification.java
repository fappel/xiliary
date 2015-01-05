package com.codeaffine.workflow.internal;

import static java.lang.String.format;

public class ArgumentVerification {

  static final String NOT_NULL_PATTERN = "Argument '%s' must not be null.";

  public static <T> T verifyNotNull( T argument, String argumentName ) {
    verifyCondition( argument != null, NOT_NULL_PATTERN, argumentName );
    return argument;
  }

  public static <T> T[] verifyNotNull( T[] arguments, String argumentName ) {
    verifyNotNull( ( Object )arguments, argumentName );
    for( int i = 0; i < arguments.length; i++ ) {
      verifyNotNull( arguments[ i ], argumentName + "[" + i + "]" );
    }
    return arguments;
  }
  
  public static void verifyCondition( boolean condition, String pattern, Object ... arguments ) {
    if( !condition ) {
      throw new IllegalArgumentException( format( pattern, arguments ) );
    }
  }
}