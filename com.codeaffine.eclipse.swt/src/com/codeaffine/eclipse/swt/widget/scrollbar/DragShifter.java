package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.widget.scrollbar.Orientation.HORIZONTAL;
import static com.codeaffine.eclipse.swt.widget.scrollbar.ShiftData.calculateSelectionRange;

import org.eclipse.swt.graphics.Point;

import com.codeaffine.eclipse.swt.widget.scrollbar.DragControl.DragAction;

final class DragShifter implements DragAction {

  private final FlatScrollBar scrollBar;

  public DragShifter( FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
  }

  @Override
  public void run( int startX, int startY, int currentX, int currentY ) {
    ShiftData shiftData = newShiftData( startX, startY, currentX, currentY );
    if( shiftData.canShift() ) {
      int selectionRange = calculateSelectionRange( scrollBar );
      int selectionDelta = shiftData.calculateSelectionDelta( selectionRange );
      scrollBar.setSelectionInternal( scrollBar.getSelection() + selectionDelta );
    }
  }

  private ShiftData newShiftData( int startX, int startY, int currentX, int currentY ) {
    ShiftData result;
    if( scrollBar.orientation == HORIZONTAL ) {
      result = new ShiftData( getScrollBarSize().x, getDragSize().x, currentX - startX );
    } else {
      result = new ShiftData( getScrollBarSize().y, getDragSize().y, currentY - startY );
    }
    return result;
  }

  private Point getScrollBarSize() {
    return scrollBar.getControl().getSize();
  }

  private Point getDragSize() {
    return scrollBar.drag.getControl().getSize();
  }
}