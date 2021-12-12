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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ShiftDataTest {

  private static final int BUTTON_LENGTH = 17;

  @Test
  public void canShift() {
    ShiftData shiftData = new ShiftData( BUTTON_LENGTH, 60, 2, 2 );

    boolean actual = shiftData.canShift();

    assertThat( actual ).isTrue();
  }

  @Test
  public void canShiftIfShifterIsToSmall() {
    ShiftData shiftData = new ShiftData( BUTTON_LENGTH, 30, 2, 2 );

    boolean actual = shiftData.canShift();

    assertThat( actual ).isFalse();
  }

  @Test
  public void calculateSelectionDelta() {
    ShiftData shiftData = new ShiftData( BUTTON_LENGTH, 60, 2, 2 );

    int actual = shiftData.calculateSelectionDelta( 30 );

    assertThat( actual ).isEqualTo( 2 );
  }

  @Test
  public void calculateSelectionRange() {
    FlatScrollBar scrollBar = mock( FlatScrollBar.class );
    when( scrollBar.getMaximum() ).thenReturn( 100 );
    when( scrollBar.getMinimum() ).thenReturn( 10 );
    when( scrollBar.getThumb() ).thenReturn( 80 );

    int actual = ShiftData.calculateSelectionRange( scrollBar );

    assertThat( actual ).isEqualTo( 10 );
  }
}