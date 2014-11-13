package com.codeaffine.eclipse.swt.widget.scrollbar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class SelectionRasterTest {

  public static final int IMAGE_SIDE_LENGTH = 20;

  private FlatScrollBar scrollBar;
  private SelectionRaster raster;

  @Before
  public void setUp() {
    scrollBar = mock( FlatScrollBar.class );
    raster = new SelectionRaster( scrollBar, IMAGE_SIDE_LENGTH );
  }

  @Test
  public void updateSelection() {
    raster.updateSelection( IMAGE_SIDE_LENGTH + 2 );

    verify( scrollBar ).setSelection( IMAGE_SIDE_LENGTH );
  }

  @Test
  public void updateSelectionIfSelectionBelongsToCurrent() {
    raster.updateSelection( IMAGE_SIDE_LENGTH / 2 );

    verify( scrollBar, never() ).setSelection( anyInt() );
  }

  @Test
  public void calculateRasterValue() {
    int actual = raster.calculateRasterValue( IMAGE_SIDE_LENGTH + 2 );

    assertThat( actual ).isEqualTo( IMAGE_SIDE_LENGTH );
  }
}