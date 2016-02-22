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
package com.codeaffine.eclipse.core.runtime.test.util;

class AssertMessageHelper {

  static String format( String pattern, Object ... arguments ) {
    return String.format( pattern, quoteIfString( arguments ) );
  }

  private static Object[] quoteIfString( Object... arguments ) {
    Object[] result = new Object[ arguments.length ];
    for( int i = 0; i < arguments.length; i++ ) {
      result[ i ] = quoteIfString( arguments[ i ] );
    }
    return result;
  }

  private static Object quoteIfString( Object argument ) {
    Object result;
    if( argument instanceof String ) {
      result = quote( ( String )argument );
    } else {
      result = argument;
    }
    return result;
  }

  private static String quote( String string ) {
    return "\"" + string + "\"";
  }
}