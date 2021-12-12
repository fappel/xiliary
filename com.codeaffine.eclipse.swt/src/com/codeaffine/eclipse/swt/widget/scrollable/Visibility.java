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
package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

class Visibility {

  private final int orientation;

  private boolean visibility;

  Visibility( int orientation ) {
    this.orientation = orientation;
  }

  boolean hasChanged( AdaptionContext<?> context ) {
    return visibility != isScrollBarVisible( context );
  }

  void update( AdaptionContext<?> context ) {
    visibility = isScrollBarVisible( context );
  }

  boolean isVisible() {
    return visibility;
  }

  private boolean isScrollBarVisible( AdaptionContext<?> context ) {
    boolean result = context.isHorizontalBarVisible();
    if( orientation == SWT.VERTICAL ) {
      result = context.isVerticalBarVisible();
    }
    return result;
  }
}