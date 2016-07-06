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

import static com.codeaffine.eclipse.swt.widget.navigationbar.ActionControlImageAdapter.load;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class ActionControlImageAdapterTest {

  private static final String UNKNOWN_IMAGE = "unknown";
  private static final String ANY_IMAGE = "anyImage";

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ActionControlImageAdapter adapter;
  private ImageProvider imageProvider;
  private Map<String, Image> images;

  @Before
  public void setUp() {
    imageProvider = mock( ImageProvider.class );
    adapter = new ActionControlImageAdapter( imageProvider );
    images = new HashMap<>();
    displayHelper.ensureDisplay();
  }

  @After
  public void tearDown() {
    images.values().forEach( image -> image.dispose() );
    adapter.dispose();
  }

  @Test
  public void getPlusImage() {
    Image actual = adapter.getImage( ActionControlImageAdapter.PLUS );

    assertThat( dataOf( actual ) ).isEqualTo( dataOf( "plus.png" ) );
  }

  @Test
  public void getMinusImage() {
    Image actual = adapter.getImage( ActionControlImageAdapter.MINUS );

    assertThat( dataOf( actual ) ).isEqualTo( dataOf( "minus.png" ) );
  }

  @Test
  public void getArrowDownImage() {
    Image actual = adapter.getImage( ActionControlImageAdapter.ARROW_DOWN );

    assertThat( dataOf( actual ) ).isEqualTo( dataOf( "arrow-down.png" ) );
  }

  @Test
  public void getBufferedImage() {
    Image first = adapter.getImage( ActionControlImageAdapter.ARROW_DOWN );
    Image second = adapter.getImage( ActionControlImageAdapter.ARROW_DOWN );

    assertThat( first ).isSameAs( second );
  }

  @Test
  public void dispose() {
    Image image = adapter.getImage( ActionControlImageAdapter.ARROW_DOWN );

    adapter.dispose();

    assertThat( image.isDisposed() ).isTrue();
  }

  @Test
  public void getAnyImage() {
    Image expected = displayHelper.createImage( 1, 1 );
    when( imageProvider.getImage( ANY_IMAGE ) ).thenReturn( expected );

    Image actual = adapter.getImage( ANY_IMAGE );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void loadUnknownImage() {
    Throwable actual = thrownBy( () -> ActionControlImageAdapter.load( UNKNOWN_IMAGE ) );

    assertThat( actual )
      .hasMessage( format( ActionControlImageAdapter.IMAGE_COULD_NOT_BE_LOADED, UNKNOWN_IMAGE ) )
      .isInstanceOf( IllegalArgumentException.class );
  }

  private byte[] dataOf( String imageName ) {
    Image image = load( imageName );
    images.put( imageName, image );
    return dataOf( image );
  }

  private static byte[] dataOf( Image actual ) {
    return actual.getImageData().data;
  }
}