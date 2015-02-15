package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Text;

class TestScrollableFactory implements ScrollableFactory<Scrollable> {

  private Scrollable scrollable;

  @Override
  public Scrollable create( Composite parent ) {
    scrollable = new Text( parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL );
    return scrollable;
  }

  Scrollable getScrollable() {
    return scrollable;
  }
}