package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class ScrollBarAdapter extends ScrollBar {

  private FlatScrollBar scrollBar;

  private ScrollBarAdapter() {
    super( null, -1 );
  }

  public void setScrollbar( FlatScrollBar scrollBar ) {
    this.scrollBar = scrollBar;
  }

  @Override
  public Point getSize() {
    return scrollBar.getSize();
  }

  @Override
  public void setSelection( int selection ) {
    scrollBar.setSelection( selection );
    scrollBar.notifyListeners( SWT.None );
  }

  @Override
  public int getSelection() {
    return scrollBar.getSelection();
  }

  @Override
  public void addListener( int eventType, Listener listener ) {
    if( eventType == SWT.Selection ) {
      scrollBar.addListener( eventType, listener );
    } else {
      super.addListener( eventType, listener );
    }
  }

  @Override
  public boolean isVisible() {
    return scrollBar.isVisible();
  }
}