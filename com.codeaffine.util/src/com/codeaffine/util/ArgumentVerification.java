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
package com.codeaffine.util;

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