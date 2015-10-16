package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

class ItemHeightMeasurementEnabler {

  private final Scrollable scrollable;
  private final Composite adapter;

  boolean onMeasurement;
  int intermediateHeightBuffer;
  int height;

  ItemHeightMeasurementEnabler( Scrollable scrollable, Composite adapter ) {
    this.scrollable = scrollable;
    this.adapter = adapter;
    this.height = calculateDefaultHeight( scrollable );
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
      adapter.getParent().layout();
    }
  }

  private void reparentScrollable( boolean redraw, Composite parent ) {
    scrollable.setRedraw( redraw );
    scrollable.setParent( parent );
  }

  private boolean needPreparations( Event event ) {
    return scrollable.getListeners( SWT.MeasureItem ).length > 1 && event.height != height;
  }

  private static int calculateDefaultHeight( Scrollable scrollable ) {
    if( scrollable instanceof Table ) {
      return ( ( Table )scrollable ).getItemHeight();
    }
    if( scrollable instanceof Tree ) {
      return ( ( Tree )scrollable ).getItemHeight();
    }
    throw new IllegalArgumentException( "Unsupported scrollable type: " + scrollable.getClass().getName() );
  }
}