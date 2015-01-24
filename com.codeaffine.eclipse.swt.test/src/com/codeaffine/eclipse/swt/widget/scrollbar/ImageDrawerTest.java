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
  public void getColor() {
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );

    Color actual = drawer.getColor();

    assertThat( actual ).isNotNull();
  }

  @Test
  public void setColorWithNullAsParameter() {
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );
    Color expected = drawer.getColor();

    drawer.setColor( null );
    Color actual = drawer.getColor();

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void draw() {
    ImageDrawer drawer = new ImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );
    drawer.setColor( displayHelper.getDisplay().getSystemColor( SWT.COLOR_BLUE ) );

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
    drawer.setColor( displayHelper.getDisplay().getSystemColor( SWT.COLOR_BLUE ) );
    Image second = drawer.draw( WIDTH, HEIGHT );

    assertThat( first.getImageData().data ).isNotEqualTo( second.getImageData().data );
  }

  private byte[] emptyImageDataArray() {
    return displayHelper.createImage( WIDTH, HEIGHT ).getImageData().data;
  }
}