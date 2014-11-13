package com.codeaffine.eclipse.swt.widget.scrollbar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ShiftDataTest {

  @Test
  public void canShift() {
    ShiftData shiftData = new ShiftData( 60, 2, 2 );

    boolean actual = shiftData.canShift();

    assertThat( actual ).isTrue();
  }

  @Test
  public void canShiftIfShifterIsToSmall() {
    ShiftData shiftData = new ShiftData( 30, 2, 2 );

    boolean actual = shiftData.canShift();

    assertThat( actual ).isFalse();
  }

  @Test
  public void calculateSelectionDelta() {
    ShiftData shiftData = new ShiftData( 60, 2, 2 );

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