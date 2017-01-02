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
      super.layout( composite, flushCache );
      updateAdapterBounds( computeAdapterBounds() );
    } );
  }

  private Rectangle computeAdapterBounds() {
    Rectangle scrollableBounds = scrollable.getBounds();
    Rectangle adapterBounds = adapter.getBounds();
    int x = scrollableBounds.x - adapterBounds.x;
    int y = scrollableBounds.y - adapterBounds.y;
    return new Rectangle( x, y, scrollableBounds.width, scrollableBounds.height );
  }

  private void updateAdapterBounds( Rectangle bounds ) {
    adapter.setBounds( bounds );
    // Note [fappel]: This passage isn't covered with unit tests, since I wasn't able to reproduce the layout
    //                issue in a test scenario. However, since this workaround seems to avoid the location computation
    //                problems with StyledTextAdapters when lazy loading parts with TextViewers I leave it as
    //                it is for now.
    if( !adapter.getBounds().equals( bounds ) ) {
      adapter.setBounds( bounds );
    }
  }
}