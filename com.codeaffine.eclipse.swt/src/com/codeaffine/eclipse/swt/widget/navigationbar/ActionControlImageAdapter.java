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
package com.codeaffine.eclipse.swt.widget.navigationbar;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

class ActionControlImageAdapter implements ImageProvider {

  static final String IMAGE_COULD_NOT_BE_LOADED = "Image <%s> could not be loaded.";

  static final String ARROW_DOWN = ActionControlImageAdapter.class.getName() + "#arrow-down";
  static final String MINUS = ActionControlImageAdapter.class.getName() + "#minus";
  static final String PLUS = ActionControlImageAdapter.class.getName() + "#plus";

  private final ImageProvider imageProvider;
  private final Map<String, Image> images;

  ActionControlImageAdapter( ImageProvider imageProvider ) {
    this.imageProvider = imageProvider;
    this.images = new HashMap<>();
  }

  @Override
  public Image getImage( String imageName ) {
    if( asList( PLUS, MINUS, ARROW_DOWN ).contains( imageName ) ) {
      return getBufferedImage( imageName, () -> load( imageKeyToImageName( imageName ) ) );
    }
    return imageProvider.getImage( imageName );
  }

  void dispose() {
    images.values().forEach( image -> image.dispose() );
  }

  static Image load( String imageName ) {
    try ( InputStream imageStream = ActionControlImageAdapter.class.getResourceAsStream( imageName ) ) {
      checkInputStreamExists( imageName, imageStream );
      return new Image( Display.getCurrent(), imageStream );
    } catch( IOException shouldNotHappen ) {
      throw new IllegalStateException( format( IMAGE_COULD_NOT_BE_LOADED, imageName ), shouldNotHappen );
    }
  }

  private static void checkInputStreamExists( String imageName, InputStream imageStream ) {
    if( imageStream == null ) {
      throw new IllegalArgumentException( format( IMAGE_COULD_NOT_BE_LOADED, imageName ) );
    }
  }

  private Image getBufferedImage( String imageName, Supplier<Image> loader ) {
    if( !images.containsKey( imageName ) ) {
      images.put( imageName, loader.get() );
    }
    return images.get( imageName );
  }

  private static String imageKeyToImageName( String key ) {
    return key.split( "#" )[ 1 ] + ".png";
  }
}