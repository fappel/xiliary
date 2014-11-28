package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

public interface ScrollableFactory<T extends Scrollable> {
  T create( Composite parent );
}