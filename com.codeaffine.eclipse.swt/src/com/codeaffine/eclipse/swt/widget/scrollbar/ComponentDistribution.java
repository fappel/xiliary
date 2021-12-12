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

import static java.lang.Math.max;

import java.math.BigDecimal;
import java.math.RoundingMode;

class ComponentDistribution {

  private static final int MIN_DRAG_LENGTH = 17;

  final int upFastLength;
  final int dragStart;
  final int dragLength;
  final int downFastStart;
  final int downFastLength;
  final int downStart;
  final int buttonLen;

  ComponentDistribution( int buttonLen, int len, int range, int pos, int thumb ) {
    int slideLen = slideLen( buttonLen, len );
    int relDragLen = relDragLen( slideLen, range, thumb );
    int minDragLength = max( MIN_DRAG_LENGTH, buttonLen );
    int interval = interval( range, relDragLen, minDragLength );
    this.dragLength = dragLen( minDragLength, relDragLen );
    this.upFastLength = upFastLen( minDragLength, interval, pos, slideLen, relDragLen, dragLength );
    this.downStart = downStart( buttonLen, len );
    this.downFastStart = downFastStart( buttonLen, upFastLength, dragLength );
    this.dragStart = dragStart( buttonLen, upFastLength );
    this.downFastLength = downFastLen( minDragLength, interval, pos, slideLen, relDragLen, dragLength, upFastLength );
    this.buttonLen = buttonLen;
  }

  private static int slideLen( int buttonLen, int len ) {
    return len - buttonLen * 2;
  }

  private static int relDragLen( int slideLen, int range, int thumb ) {
    return divide( slideLen * thumb, range );
  }

  private static int interval( int range, int relDragLen, int minDragLength ) {
    int result = range;
    if( useMinDragLen( minDragLength, relDragLen ) ) {
      result += minDragLength - relDragLen / 2;
    }
    return result;
  }

  private static int dragLen( int buttonLen, int relDragLen   ) {
    return max( relDragLen, buttonLen );
  }

  private static int upFastLen( int buttonLen, int range, int pos, int slideLen, int relDragLen, int dragLen ) {
    int result = slideLen * pos / range;
    if( useMinDragLen( buttonLen, relDragLen ) ) {
      result -= divide( ( dragLen - relDragLen ) * pos, range );
    }
    return result;
  }

  private static int downStart( int buttonLen, int len ) {
    return len - buttonLen;
  }

  private static int downFastStart( int buttonLen, int upFastLength, int dragLength ) {
    return buttonLen + upFastLength + dragLength;
  }

  private static int dragStart( int buttonLen, int upFastLen ) {
    return buttonLen + upFastLen;
  }

  private static int downFastLen(
    int buttonLen, int range, int pos, int slideLen, int relDragLen, int dragLen, int upFastLen )
  {
    int result = divide( slideLen * ( range - pos ), range ) - dragLen;
    if( useMinDragLen( buttonLen, relDragLen ) ) {
      result += divide( ( dragLen - relDragLen ) * pos, range );
    }
    return adjustDownFastLen( result, slideLen, dragLen, upFastLen );
  }

  private static boolean useMinDragLen( int buttonLen, int relDragLen ) {
    return relDragLen < buttonLen;
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