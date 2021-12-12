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
package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.widget.scrollbar.ImageDrawer.IMAGE_DRAWER_IS_DISPOSED;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class ImageDrawerTest {

  private static final int HEIGHT = 30;
  private static final int WIDTH = 20;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Before
  public void setUp() {
    displayHelper.ensureDisplay();
  }

  @Test
  public void getForeground() {
    Color color = displayHelper.getSystemColor( SWT.COLOR_BLUE );
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION, color, color );

    Color actual = drawer.getForeground();

    assertThat( actual )
      .isNotSameAs( color )
      .isNotNull();
  }

  @Test
  public void getForegroundIfDisposed() {
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );
    drawer.dispose();

    Throwable actual = thrownBy( () -> drawer.getForeground() );

    assertThat( actual )
      .hasMessage( ImageDrawer.IMAGE_DRAWER_IS_DISPOSED )
      .isInstanceOf( IllegalStateException.class );
  }

  @Test
  public void getBackground() {
    Color color = displayHelper.getSystemColor( SWT.COLOR_BLUE );
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION, color, color );

    Color actual = drawer.getBackground();

    assertThat( actual )
      .isNotSameAs( color )
      .isNotNull();
  }

  @Test
  public void getBackgroundIfDisposed() {
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );
    drawer.dispose();

    Throwable actual = thrownBy( () -> drawer.getBackground() );

    assertThat( actual )
      .hasMessage( ImageDrawer.IMAGE_DRAWER_IS_DISPOSED )
      .isInstanceOf( IllegalStateException.class );
  }

  @Test
  public void setForeground() {
    Color expected = displayHelper.getSystemColor( SWT.COLOR_BLUE );
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );

    drawer.setForeground( expected );
    Color actual = drawer.getForeground();

    assertThat( actual )
      .isEqualTo( expected )
      .isNotSameAs( expected );
  }

  @Test
  public void setForegroundIfDisposed() {
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );
    drawer.dispose();

    Throwable actual = thrownBy( () -> drawer.setForeground( displayHelper.getSystemColor( SWT.COLOR_BLUE ) ) );

    assertThat( actual )
      .isInstanceOf( IllegalStateException.class )
      .hasMessage( IMAGE_DRAWER_IS_DISPOSED );
  }

  @Test
  public void setForegroundWithNullAsParameter() {
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );
    Color expected = drawer.getForeground();

    drawer.setForeground( null );
    Color actual = drawer.getForeground();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void setBackground() {
    Color expected = displayHelper.getSystemColor( SWT.COLOR_BLUE );
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );

    drawer.setBackground( expected );
    Color actual = drawer.getBackground();

    assertThat( actual )
      .isEqualTo( expected )
      .isNotSameAs( expected );
  }

  @Test
  public void setBackgroundIfDisposed() {
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );
    drawer.dispose();

    Throwable actual = thrownBy( () -> drawer.setBackground( displayHelper.getSystemColor( SWT.COLOR_BLUE ) ) );

    assertThat( actual )
      .isInstanceOf( IllegalStateException.class )
      .hasMessage( IMAGE_DRAWER_IS_DISPOSED );
  }

  @Test
  public void setBackgroundWithNullAsParameter() {
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );
    Color expected = drawer.getBackground();

    drawer.setBackground( null );
    Color actual = drawer.getBackground();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void draw() {
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );
    drawer.setForeground( displayHelper.getDisplay().getSystemColor( SWT.COLOR_BLUE ) );

    Image actual = drawer.draw( WIDTH, HEIGHT );

    assertThat( actual.getBounds() ).isEqualTo( new Rectangle( 0, 0, WIDTH, HEIGHT ) );
    assertThat( actual.getDevice() ).isEqualTo( Display.getCurrent() );
    assertThat( actual.getImageData().data ).isNotEqualTo( emptyImageDataArray() );
  }

  @Test
  public void drawIfDisposed() {
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );
    drawer.dispose();

    Throwable actual = thrownBy( () -> drawer.draw( WIDTH, HEIGHT ) );

    assertThat( actual )
      .isInstanceOf( IllegalStateException.class )
      .hasMessage( IMAGE_DRAWER_IS_DISPOSED );
  }

  @Test
  public void drawWithDifferentColors() {
    displayHelper.ensureDisplay();

    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );
    Image first = drawer.draw( WIDTH, HEIGHT );
    drawer.setForeground( displayHelper.getDisplay().getSystemColor( SWT.COLOR_BLUE ) );
    Image second = drawer.draw( WIDTH, HEIGHT );
    drawer.setBackground( displayHelper.getDisplay().getSystemColor( SWT.COLOR_RED ) );
    Image third = drawer.draw( WIDTH, HEIGHT );

    assertThat( first.getImageData().data ).isNotEqualTo( second.getImageData().data );
    assertThat( second.getImageData().data ).isNotEqualTo( third.getImageData().data );
  }

  @Test
  public void dispose() {
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );

    drawer.dispose();
    boolean actual = drawer.isDisposed();

    assertThat( actual ).isTrue();
  }

  @Test
  public void disposeTwice() {
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );

    drawer.dispose();
    Throwable actual = thrownBy( () -> drawer.dispose() );

    assertThat( actual ).isNull();
  }

  private byte[] emptyImageDataArray() {
    return displayHelper.createImage( WIDTH, HEIGHT ).getImageData().data;
  }
}