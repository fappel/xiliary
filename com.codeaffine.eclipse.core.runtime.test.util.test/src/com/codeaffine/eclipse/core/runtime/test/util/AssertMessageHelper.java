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