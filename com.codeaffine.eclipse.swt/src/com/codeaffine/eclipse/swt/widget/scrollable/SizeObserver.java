package com.codeaffine.eclipse.swt.widget.scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.AdaptionContext;

interface SizeObserver {
  boolean mustLayoutAdapter( AdaptionContext<?> context );
  void update( AdaptionContext<?> context );
}