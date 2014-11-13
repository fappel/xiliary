package com.codeaffine.eclipse.swt.widget.scrollbar;

import static java.lang.Math.max;

import java.math.BigDecimal;
import java.math.RoundingMode;

class ComponentDistribution {

  public static final int BUTTON_LENGTH = 17;

  public final int upFastLength;
  public final int dragStart;
  public final int dragLength;
  public final int downFastStart;
  public final int downFastLength;
  public final int downStart;

  public ComponentDistribution( int len, int range, int pos, int thumb ) {
    int slideLen = slideLen( len );
    int relDragLen = relDragLen( slideLen, range, thumb );
    this.dragLength = dragLen( relDragLen );
    this.upFastLength = upFastLen( range, pos, slideLen, relDragLen, dragLength );
    this.downStart = downStart( len );
    this.downFastStart = downFastStart( upFastLength, dragLength );
    this.dragStart = dragStart( upFastLength );
    this.downFastLength = downFastLen( range, pos, slideLen, relDragLen, dragLength, upFastLength );
  }

  private static int slideLen( int len ) {
    return len - BUTTON_LENGTH * 2;
  }

  private static int relDragLen( int slideLen, int range, int thumb ) {
    return divide( slideLen * thumb, range );
  }

  private static int dragLen( int relDragLen ) {
    return max( relDragLen, BUTTON_LENGTH );
  }

  private static int upFastLen( int range, int pos, int slideLen, int relDragLen, int dragLen ) {
    int result = slideLen * pos / range;
    if( useMinDragLen( relDragLen ) ) {
      result -= divide( ( dragLen - relDragLen ) * pos, range );
    }
    return result;
  }

  private static int downStart( int len ) {
    return len - BUTTON_LENGTH;
  }

  private static int downFastStart( int upFastLength, int dragLength ) {
    return BUTTON_LENGTH + upFastLength + dragLength;
  }

  private static int dragStart( int upFastLen ) {
    return BUTTON_LENGTH + upFastLen;
  }

  private static int downFastLen(
    int range, int pos, int slideLen, int relDragLen, int dragLen, int upFastLen )
  {
    int result = divide( slideLen * ( range - pos ), range ) - dragLen;
    if( useMinDragLen( relDragLen ) ) {
      result += divide( ( dragLen - relDragLen ) * pos, range );
    }
    return adjustDownFastLen( result, slideLen, dragLen, upFastLen );
  }

  private static boolean useMinDragLen( int relDragLen ) {
    return relDragLen < BUTTON_LENGTH;
  }

  static int divide( int dividend, int divisor ) {
    BigDecimal bigDividend = new BigDecimal( dividend );
    BigDecimal bigDivisor = new BigDecimal( divisor );
    return bigDividend .divide( bigDivisor, 0, RoundingMode.HALF_EVEN ) .intValue();
  }

  private static int adjustDownFastLen( int tentative, int slideLen, int dragLen, int upFastLen ) {
    // TODO [fappel]: Without this there is a flickering of the downFast label of one pixel.
    //                Check whether this can be resolved by better rounding or whatsoever.
    int result = tentative;
    if( slideLen < upFastLen + dragLen + result ) {
      result--;
    } else if( slideLen > upFastLen + dragLen + result ) {
      result++;
    }
    return result;
  }
}