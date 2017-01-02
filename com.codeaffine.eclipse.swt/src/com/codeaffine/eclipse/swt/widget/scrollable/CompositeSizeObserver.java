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
package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

class CompositeSizeObserver implements SizeObserver {

  private final Composite composite;

  private boolean mustLayoutAdapter;

  public CompositeSizeObserver( Composite composite ) {
    this.composite = composite;
    this.composite.addListener( SWT.Resize, evt -> mustLayoutAdapter = true );
  }

  @Override
  public boolean mustLayoutAdapter( AdaptionContext<?> context ) {
    boolean result = mustLayoutAdapter;
    mustLayoutAdapter = false;
    return result;
  }

  @Override
  public void update( AdaptionContext<?> context ) {
  }
}