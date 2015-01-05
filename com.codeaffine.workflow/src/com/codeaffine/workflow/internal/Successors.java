package com.codeaffine.workflow.internal;

class Successors {

  static final String[] EMYPTY_SUCCESSORS = new String[ 0 ];

  static String[] toSuccessors( String successor ) {
    return successor != null ? new String[] { successor } : EMYPTY_SUCCESSORS;
  }

  static String[] concat( String successor1, String successor2, String... successors ) {
    String[] result = new String[ successors.length + 2 ];
    result[ 0 ] = successor1;
    result[ 1 ] = successor2;
    for( int i = 0; i < successors.length; i++ ) {
      result[ i + 2 ] = successors[ i ];
    }
    return result;
  }
}