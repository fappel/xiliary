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

import static com.codeaffine.eclipse.swt.util.ControlReflectionUtil.PARENT;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.codeaffine.eclipse.swt.util.ControlReflectionUtil;
import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

class ItemHeightMeasurementEnabler {

  static final int UNSET = -1;

  private final Listener ownerDrawInUseWatchDog;
  private final ScrollableControl<?> scrollable;
  private final ScrollableRedrawState redrawState;
  private final Composite adapter;
  private final Listener prepare;
  private final Listener restore;

  int intermediateHeightBuffer;
  boolean skipEnsureRestore;
  boolean onMeasurement;
  int height;

  ItemHeightMeasurementEnabler( ScrollableControl<?> scrollable, Composite adapter ) {
    this.scrollable = scrollable;
    this.adapter = adapter;
    this.height = scrollable.getItemHeight();
    this.redrawState = new ScrollableRedrawState( scrollable );
    this.ownerDrawInUseWatchDog = evt -> registerListenerOnMeasurementEvent( evt );
    this.prepare = evt -> prepareScrollableToAllowProperHeightMeasurement( evt );
    this.restore = evt -> restoreScrollableAfterMeasurement();
    this.intermediateHeightBuffer = UNSET;
    registerMeasurementWatchDog( scrollable );
  }

  private void registerListenerOnMeasurementEvent( Event event ) {
    if( scrollable.isSameAs( event.widget ) && scrollable.isOwnerDrawn() ) {
      scrollable.addListener( SWT.MeasureItem, prepare );
      scrollable.addListener( SWT.EraseItem, restore );
      scrollable.getDisplay().removeFilter( SWT.MeasureItem, this.ownerDrawInUseWatchDog );
      scrollable.getDisplay().timerExec( 50, () -> { ensureRestore(); } );
    }
  }

  private void registerMeasurementWatchDog( ScrollableControl<?> scrollable ) {
    Display display = scrollable.getDisplay();
    display.addFilter( SWT.MeasureItem, ownerDrawInUseWatchDog );
    scrollable.addListener( SWT.Dispose, evt -> display.removeFilter( SWT.MeasureItem, ownerDrawInUseWatchDog ) );
  }

  private void prepareScrollableToAllowProperHeightMeasurement( Event event ) {
    if( needPreparations( event ) ) {
      reparentScrollable( false, adapter.getParent() );
      shiftHeightStateBuffer( event );
      onMeasurement = true;
    }
  }

  private void shiftHeightStateBuffer( Event event ) {
    if( event.height == intermediateHeightBuffer ) {
      height = intermediateHeightBuffer;
    }
    if( intermediateHeightBuffer == UNSET ) {
      intermediateHeightBuffer = event.height;
    }
  }

  private void ensureRestore() {
    if( onMeasurement && !skipEnsureRestore ) {
      height = intermediateHeightBuffer;
      restoreScrollableAfterMeasurement();
    }
  }

  private void restoreScrollableAfterMeasurement() {
    if( onMeasurement ) {
      skipEnsureRestore = true;
      reparentScrollable( true, adapter );
      reAdjustParentReferenceOfScrollableAfterMeasurement();
      onMeasurement = false;
      adapter.getParent().layout();
    }
  }

  private void reparentScrollable( boolean redraw, Composite parent ) {
    redrawState.update( redraw );
    scrollable.setParent( parent );
  }

  private void reAdjustParentReferenceOfScrollableAfterMeasurement() {
    if( height == intermediateHeightBuffer ) {
      new ControlReflectionUtil().setField( scrollable.getControl(), PARENT, adapter.getParent() );
      scrollable.removeListener( SWT.MeasureItem, prepare );
      scrollable.removeListener( SWT.EraseItem, restore );
    }
  }

  private boolean needPreparations( Event event ) {
    return event.height != height;
  }
}