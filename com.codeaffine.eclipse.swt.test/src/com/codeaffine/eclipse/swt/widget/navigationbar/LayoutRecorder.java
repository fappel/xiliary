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
package com.codeaffine.eclipse.swt.widget.navigationbar;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

class LayoutRecorder extends Layout {

  private final AtomicBoolean layoutPerformedStatus;

  public LayoutRecorder( AtomicBoolean layoutPerformedStatus ) {
    this.layoutPerformedStatus = layoutPerformedStatus;
  }

  @Override
  protected void layout( Composite composite, boolean flushCache ) {
    layoutPerformedStatus.set( true );
  }

  @Override
  protected Point computeSize( Composite composite, int wHint, int hHint, boolean flushCache ) {
    return new Point( 0 , 0 );
  }
}