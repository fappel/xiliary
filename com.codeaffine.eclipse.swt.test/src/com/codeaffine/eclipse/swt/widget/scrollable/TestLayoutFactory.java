/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

class TestLayoutFactory implements LayoutFactory<Scrollable> {

  private final Layout expected;

  TestLayoutFactory( Layout expected ) {
    this.expected = expected;
  }

  @Override
  public Layout create( AdaptionContext<Scrollable> context ) {
    return expected;
  }

  @Override
  public void setIncrementButtonLength( int length ) {
  }

  @Override
  public int getIncrementButtonLength() {
    return 0;
  }

  @Override
  public void setIncrementColor( Color color ) {
  }

  @Override
  public Color getIncrementColor() {
    return null;
  }

  @Override
  public void setPageIncrementColor( Color color ) {
  }

  @Override
  public Color getPageIncrementColor() {
    return null;
  }

  @Override
  public void setThumbColor( Color color ) {
  }

  @Override
  public Color getThumbColor() {
    return null;
  }

  @Override
  public ScrollBar getVerticalBarAdapter() {
    return null;
  }

  @Override
  public ScrollBar getHorizontalBarAdapter() {
    return null;
  }

  @Override
  public void setBackgroundColor( Color color ) {
  }

  @Override
  public Color getBackgroundColor() {
    return null;
  }

  @Override
  public void setDemeanor( Demeanor demeanor ) {
  }

  @Override
  public Demeanor getDemeanor() {
    return null;
  }
}