package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

class ItemHeightMeasurementEnabler {

  private final Scrollable scrollable;
  private final Composite adapter;
  private final Listener ownerDrawInUseWatchDog;

  int intermediateHeightBuffer;
  boolean onMeasurement;
  int height;

  ItemHeightMeasurementEnabler( Scrollable scrollable, Composite adapter ) {
    this.scrollable = scrollable;
    this.adapter = adapter;
    this.height = calculateDefaultHeight( scrollable );
    this.ownerDrawInUseWatchDog = evt -> registerListenerOnMeasurementEvent( evt );
    registerMeasurementWatchDog( scrollable );
  }

  private void registerListenerOnMeasurementEvent( Event event ) {
    if( event.widget == scrollable && scrollable.getListeners( SWT.MeasureItem ).length != 0 ) {
      scrollable.addListener( SWT.MeasureItem, evt -> prepareScrollableToAllowProperHeightMeasurement( evt ) );
      scrollable.addListener( SWT.EraseItem, evt -> restoreScrollableAfterMeasurement() );
      scrollable.getDisplay().removeFilter( SWT.MeasureItem, this.ownerDrawInUseWatchDog );
    }
  }

  private void registerMeasurementWatchDog( Scrollable scrollable ) {
    Display display = scrollable.getDisplay();
    display.addFilter( SWT.MeasureItem, ownerDrawInUseWatchDog );
    scrollable.addDisposeListener( evt -> display.removeFilter( SWT.MeasureItem, ownerDrawInUseWatchDog ) );
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
    return event.height != height;
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