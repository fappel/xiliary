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
package com.codeaffine.eclipse.swt.widget.scrollable.context;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.layout.LayoutWrapper;
import com.codeaffine.eclipse.swt.util.OperationWithRedrawSuspension;

class LayoutActor extends LayoutWrapper {

  private final OperationWithRedrawSuspension operationWithRedrawSuspension;
  private final ScrollableControl<? extends Scrollable> scrollable;
  private final Composite adapter;

  LayoutActor(
    Layout layout, ScrollableControl<? extends Scrollable> scrollable, Composite adapter )
  {
    this( layout, scrollable, adapter, new OperationWithRedrawSuspension() );
  }

  LayoutActor( Layout layout,
               ScrollableControl<? extends Scrollable> scrollable,
               Composite adapter,
               OperationWithRedrawSuspension operationWithRedrawSuspension )
  {
    super( layout );
    this.operationWithRedrawSuspension = operationWithRedrawSuspension;
    this.scrollable = scrollable;
    this.adapter = adapter;
  }

  @Override
  public Point computeSize( Composite composite, int wHint, int hHint, boolean flushCache ) {
    return super.computeSize( composite, wHint, hHint, flushCache );
  }

  @Override
  public void layout( Composite composite, boolean flushCache ) {
    operationWithRedrawSuspension.execute( composite, () -> {
      Rectangle oldBounds = scrollable.getBounds();
      super.layout( composite, flushCache );
      adapter.setBounds( computeAdapterBounds( oldBounds, scrollable.getBounds() ) );
    } );
  }

  private static Rectangle computeAdapterBounds( Rectangle oldBounds, Rectangle newBounds ) {
    return new Rectangle( newBounds.x - oldBounds.x, newBounds.y - oldBounds.y, newBounds.width, newBounds.height );
  }
}