package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Scrollable;

class ItemHeightMeasurementEnabler {

  private final Scrollable scrollable;
  private final Composite adapter;

  private boolean onMeasurement;
  private int intermediateHeightBuffer;
  private int height;

  ItemHeightMeasurementEnabler( Scrollable scrollable, Composite adapter ) {
    this.scrollable = scrollable;
    this.adapter = adapter;
    register( scrollable );
  }

  private void register( Scrollable scrollable ) {
    scrollable.addListener( SWT.MeasureItem, evt -> prepareScrollableToAllowProperHeightMeasurement( evt ) );
    scrollable.addListener( SWT.EraseItem, evt -> restoreScrollableAfterMeasurement() );
  }

  private void prepareScrollableToAllowProperHeightMeasurement( Event event ) {
    if( needPreparations( event ) ) {
      reparentScrollable( false, adapter.getParent() );
      shiftHeightStateBuffer( event );
      onMeasurement = true;
    }
  }

  private void shiftHeightStateBuffer( Event event ) {
    height = intermediateHeightBuffer;
    intermediateHeightBuffer = event.height;
  }

  private void restoreScrollableAfterMeasurement() {
    if( onMeasurement ) {
      reparentScrollable( true, adapter );
      onMeasurement = false;
    }
  }

  private void reparentScrollable( boolean redraw, Composite parent ) {
    scrollable.setRedraw( redraw );
    scrollable.setParent( parent );
  }

  private boolean needPreparations( Event event ) {
    return scrollable.getListeners( SWT.MeasureItem ).length > 1 && event.height != height;
  }
}