package com.codeaffine.eclipse.swt.widget.scrollbar;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class DragImageDrawerTest {

  private static final int HEIGHT = 30;
  private static final int WIDTH = 20;

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void draw() {
    displayHelper.ensureDisplay();
    DragImageDrawer drawer = new DragImageDrawer( FlatScrollBar.DEFAULT_MAX_EXPANSION );

    Image actual = drawer.draw( WIDTH, HEIGHT );

    assertThat( actual.getBounds() ).isEqualTo( new Rectangle( 0, 0, WIDTH, HEIGHT ) );
    assertThat( actual.getDevice() ).isEqualTo( Display.getCurrent() );
    assertThat( actual.getImageData().data ).isNotEqualTo( emptyImageDataArray() );
  }

  private byte[] emptyImageDataArray() {
    return displayHelper.createImage( WIDTH, HEIGHT ).getImageData().data;
  }
}