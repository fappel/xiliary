package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

interface LayoutFactory<T extends Scrollable> extends ScrollbarStyle {
  Layout create( AdaptionContext<T> context );
  ScrollBar getVerticalBarAdapter();
  ScrollBar getHorizontalBarAdapter();
}