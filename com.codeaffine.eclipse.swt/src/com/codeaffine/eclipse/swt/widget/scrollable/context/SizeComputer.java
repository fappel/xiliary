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
package com.codeaffine.eclipse.swt.widget.scrollable.context;

import static java.lang.Integer.valueOf;
import static java.lang.Math.max;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scrollable;

class SizeComputer {

  static final String WIDTH_OFFSET_ON_VISIBLE_HORIZONTAL_BAR = AdaptionContext.class.getName() + "#widthAdjustment:";
  static final String PREFERRED_SIZE = AdaptionContext.class.getName() + "# preferredSize";
  static final Point EMPTY_BUFFER_SIZE = new Point( 0, 0 );

  private final ScrollableControl<? extends Scrollable> scrollable;
  private final PreferredSizeProvider preferredSizeProvider;
  private final Composite adapter;

  SizeComputer( ScrollableControl<? extends Scrollable> scrollable, Composite adapter ) {
    this.preferredSizeProvider = new PreferredSizeProvider( scrollable );
    this.scrollable = scrollable;
    this.adapter = adapter;
  }

  Point getPreferredSize() {
    Point preferredSize = getPreferredSizeInternal();
    return new Point( preferredSize.x + getPreferredWidthAdjustmentForVisibleHorizontalBar(), preferredSize.y );
  }

  void updatePreferredSize() {
    Point preferredSize = preferredSizeProvider.getSize();
    if( scrollable.isInstanceof( ScrolledComposite.class ) ) {
      preferredSizeForScrolledComposite( preferredSize );
    } else if( preferredSize.x - scrollable.getVerticalBarSize().x == scrollable.getSize().x ) {
      scrollable.setData( PREFERRED_SIZE, scrollable.getSize() );
    } else if( isVirtualAndOwnerDrawn() ) {
      bestPreferredSizeGuessForVirtualAndOwnerDrawnScrollables( preferredSize );
    } else { // check possible improvement on non owner drawn scrollables: #28
      bestPreferredlSizeGuessForOwnerDrawnScrollables( preferredSize );
    }
  }

  void adjustPreferredWidthIfHorizontalBarIsVisible() {
    if( scrollable.isHorizontalBarVisible() && scrollable.isChildOf( adapter.getParent() ) ) {
      updatePreferredSize();
      scrollable.setData( getWidthOffsetKey(), valueOf( getPreferredSizeInternal().x ) );
    }
  }

  private boolean isVirtualAndOwnerDrawn() {
    return scrollable.isOwnerDrawn() && scrollable.hasStyle( SWT.VIRTUAL );
  }

  private Point getPreferredSizeInternal() {
    if( getBufferedPreferredSize().equals( EMPTY_BUFFER_SIZE ) ) {
      updatePreferredSize();
    }
    return getBufferedPreferredSize();
  }

  private Point getBufferedPreferredSize() {
    Point result = ( Point )scrollable.getData( PREFERRED_SIZE );
    return result == null ? EMPTY_BUFFER_SIZE: result;
  }

  private int getPreferredWidthAdjustmentForVisibleHorizontalBar() {
    Integer result = ( Integer )scrollable.getData( getWidthOffsetKey() );
    return result != null ? result.intValue() : 0;
  }

  private String getWidthOffsetKey() {
    return WIDTH_OFFSET_ON_VISIBLE_HORIZONTAL_BAR + getPreferredSizeInternal();
  }

  private void preferredSizeForScrolledComposite( Point preferredSize ) {
    scrollable.setData( PREFERRED_SIZE, preferredSize );
    Control content = ( ( ScrolledComposite )scrollable.getControl() ).getContent();
    if( content != null ) {
      scrollable.setData( PREFERRED_SIZE, content.getSize() );
    }
  }

  private void bestPreferredSizeGuessForVirtualAndOwnerDrawnScrollables( Point computed ) {
    int parentWidth = adapter.getClientArea().width;
    int width = max( getBufferedPreferredSize().x, parentWidth );
    scrollable.setData( PREFERRED_SIZE, new Point( width, computed.y ) );
    // Ugly side effect, might lead to flickering - better options?
    scrollable.setHorizontalBarVisible( false );
  }

  private void bestPreferredlSizeGuessForOwnerDrawnScrollables( Point computed ) {
    int parentWidth = adapter.getClientArea().width;
    int maximum = scrollable.getHorizontalBarMaximum();
    int width = max( getBufferedPreferredSize().x, max( parentWidth, maximum ) );
    scrollable.setData( PREFERRED_SIZE, new Point( width, computed.y ) );
  }
}