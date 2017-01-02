/**
 * Copyright (c) 2014 - 2017 Frank Appel
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