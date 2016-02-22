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
package com.codeaffine.eclipse.swt.widget.scrollbar;

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
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );

    Color actual = drawer.getForeground();

    assertThat( actual ).isNotNull();
  }

  @Test
  public void getBackground() {
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );

    Color actual = drawer.getBackground();

    assertThat( actual ).isNotNull();
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

  private byte[] emptyImageDataArray() {
    return displayHelper.createImage( WIDTH, HEIGHT ).getImageData().data;
  }
}