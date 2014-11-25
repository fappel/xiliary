package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.widget.scrollbar.ComponentDistributionAssert.assertThat;

import org.junit.Test;

public class ComponentDistributionTest {

  private static final int BUTTON_LENGTH = 17;

  @Test
  public void distributionWithLowestDragPosition() {
    assertThat( new ComponentDistribution( BUTTON_LENGTH, 300, 100, 0, 50 ) )
      .hasUpFastLength( 0 )
      .hasDragStart( 17 )
      .hasDragLength( 133 )
      .hasDownFastStart( 150 )
      .hasDownFastLength( 133 )
      .hasDownStart( 283 );
  }

  @Test
  public void distributionWithHighestDragPosition() {
    assertThat( new ComponentDistribution( BUTTON_LENGTH, 300, 100, 50, 50 ) )
      .hasUpFastLength( 133 )
      .hasDragStart( 150 )
      .hasDragLength( 133 )
      .hasDownFastStart( 283 )
      .hasDownFastLength( 0 )
      .hasDownStart( 283 );
  }

  @Test
  public void distributionWithDragPositionInRange() {
    assertThat( new ComponentDistribution( BUTTON_LENGTH, 300, 100, 13, 50 ) )
      .hasUpFastLength( 34 )
      .hasDragStart( 51 )
      .hasDragLength( 133 )
      .hasDownFastStart( 184 )
      .hasDownFastLength( 99 )
      .hasDownStart( 283 );
  }

  @Test
  public void distributionWithDragRangeOverflow() {
    assertThat( new ComponentDistribution( BUTTON_LENGTH, 300, 10000, 0, 50 ) )
      .hasUpFastLength( 0 )
      .hasDragStart( 17 )
      .hasDragLength( 17 )
      .hasDownFastStart( 34 )
      .hasDownFastLength( 249 )
      .hasDownStart( 283 );
  }

  @Test
  public void distributionWithUnderflowOfMinimumDragLengthAndLowestDragPosition() {
    assertThat( new ComponentDistribution( 0, 90, 720, 0, 93 ) )
      .hasUpFastLength( 0 )
      .hasDragStart( 0 )
      .hasDragLength( 17 )
      .hasDownFastStart( 17 )
      .hasDownFastLength( 73 )
      .hasDownStart( 90 );
  }

  @Test
  public void distributionWithUnderflowOfMinimumDragLengthAndHighestDragPosition() {
    assertThat( new ComponentDistribution( 0, 90, 720, 672, 93 ) )
      .hasUpFastLength( 77 )
      .hasDragStart( 77 )
      .hasDragLength( 17 )
      .hasDownFastStart( 94 )
      .hasDownFastLength( -4 )
      .hasDownStart( 90 );
  }
}