package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;

interface LayoutFactory<T extends Scrollable> {
  Layout create( Composite parent, T scrollable );
}