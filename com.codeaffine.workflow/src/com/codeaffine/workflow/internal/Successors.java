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