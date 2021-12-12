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
package com.codeaffine.eclipse.swt.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceLoader {

  private static final int BUFFER_SIZE = 8192;

  public byte[] load( String resourcePath ) {
    return load( resourcePath, getClass().getClassLoader() );
  }

  public byte[] load( String resourcePath, ClassLoader classLoader ) {
    return load( verifyExistance( classLoader.getResourceAsStream( resourcePath ), resourcePath ) );
  }

  private static byte[] load( InputStream input ) {
    try {
      return doLoad( input );
    } catch( IOException e ) {
      throw new IllegalStateException( e );
    } finally {
      closeSilently( input );
    }
  }

  private static byte[] doLoad( InputStream input ) throws IOException {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    byte[] buffer = new byte[ BUFFER_SIZE ];
    int bytesRead;
    while( ( bytesRead = input.read( buffer ) ) != -1 ) {
      result.write( buffer, 0, bytesRead );
    }
    return result.toByteArray();
  }

  private static InputStream verifyExistance( InputStream inputStream , String resourcePath  ) {
    if( inputStream == null ) {
      throw new IllegalArgumentException( "'" + resourcePath + "' does not exist." );
    }
    return inputStream;
  }

  private static void closeSilently( InputStream input ) {
    try {
      input.close();
    } catch( IOException e ) {
      throw new IllegalStateException( e );
    }
  }
}