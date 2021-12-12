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
package com.codeaffine.eclipse.swt.layout;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

public class LayoutSpy extends Layout {

  static final Point COMPUTE_SIZE_RESULT = new Point( 1, 2 );

  private RuntimeException runtimeException;
  private boolean throwRuntimeException;
  private boolean layoutInvoked;
  private Composite composite;
  private boolean flushCache;
  private int heightHint;
  private int widthHint;

  @Override
  protected Point computeSize( Composite composite, int widthHint, int heightHint, boolean flushCache ) {
    this.composite = composite;
    this.widthHint = widthHint;
    this.heightHint = heightHint;
    this.flushCache = flushCache;
    throwRuntimeExceptionIfRequested();
    return COMPUTE_SIZE_RESULT;
  }

  @Override
  protected void layout( Composite composite, boolean flushCache ) {
    this.composite = composite;
    this.flushCache = flushCache;
    layoutInvoked = true;
    throwRuntimeExceptionIfRequested();
  }

  boolean layoutHasBeenInvoked() {
    return layoutInvoked;
  }

  Composite getComposite() {
    return composite;
  }

  int getWidthHint() {
    return widthHint;
  }

  int getHeightHint() {
    return heightHint;
  }

  boolean isFlushCache() {
    return flushCache;
  }

  void throwRuntimeExceptionOnMethodInvocations() {
    throwRuntimeException = true;
  }

  RuntimeException getRuntimeException() {
    return runtimeException;
  }

  private void throwRuntimeExceptionIfRequested() {
    if( throwRuntimeException ) {
      runtimeException = new RuntimeException( "bad" );
      throw runtimeException;
    }
  }
}