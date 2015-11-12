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

  private final Listener ownerDrawInUseWatchDog;
  private final ScrollableControl<?> scrollable;
  private final Composite adapter;

  int intermediateHeightBuffer;
  boolean onMeasurement;
  int height;

  ItemHeightMeasurementEnabler( ScrollableControl<?> scrollable, Composite adapter ) {
    this.scrollable = scrollable;
    this.adapter = adapter;
    this.height = scrollable.getItemHeight();
    this.ownerDrawInUseWatchDog = evt -> registerListenerOnMeasurementEvent( evt );
    registerMeasurementWatchDog( scrollable );
  }

  private void registerListenerOnMeasurementEvent( Event event ) {
    if( scrollable.isSameAs( event.widget ) && scrollable.isOwnerDrawn() ) {
      scrollable.addListener( SWT.MeasureItem, evt -> prepareScrollableToAllowProperHeightMeasurement( evt ) );
      scrollable.addListener( SWT.EraseItem, evt -> restoreScrollableAfterMeasurement() );
      scrollable.getDisplay().removeFilter( SWT.MeasureItem, this.ownerDrawInUseWatchDog );
      scrollable.getDisplay().timerExec( 10, () -> { ensureRestore(); } );
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
    height = intermediateHeightBuffer;
    intermediateHeightBuffer = event.height;
  }

  private void ensureRestore() {
    height = intermediateHeightBuffer;
    restoreScrollableAfterMeasurement();
  }

  private void restoreScrollableAfterMeasurement() {
    if( onMeasurement ) {
      reparentScrollable( true, adapter );
      reAdjustParentReferenceOfScrollableAfterMeasurement();
      onMeasurement = false;
      adapter.getParent().layout();
    }
  }

  private void reparentScrollable( boolean redraw, Composite parent ) {
    scrollable.setRedraw( redraw );
    scrollable.setParent( parent );
  }

  private void reAdjustParentReferenceOfScrollableAfterMeasurement() {
    if( height == intermediateHeightBuffer ) {
      new ControlReflectionUtil().setField( scrollable.getControl(), PARENT, adapter.getParent() );
    }
  }

  private boolean needPreparations( Event event ) {
    return event.height != height;
  }
}