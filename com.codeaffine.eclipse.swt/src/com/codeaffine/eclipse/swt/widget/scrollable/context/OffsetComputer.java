package com.codeaffine.eclipse.swt.widget.scrollable.context;

import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.util.Platform;
import com.codeaffine.eclipse.swt.util.Platform.PlatformType;

class OffsetComputer {

  static final int DEFAULT_OFFSET = 0;
  static final int GTK_OFFSET = 4;

  private final ScrollableControl<? extends Scrollable> scrollable;
  private final Platform platform;

  OffsetComputer( ScrollableControl<? extends Scrollable> scrollable ) {
    this( scrollable, new Platform() );
  }

  OffsetComputer( ScrollableControl<? extends Scrollable> scrollable, Platform platform ) {
    this.scrollable = scrollable;
    this.platform = platform;
  }

  int compute() {
    if( matchesGtk() && !isOverlay() ) {
      return GTK_OFFSET;
    }
    return DEFAULT_OFFSET;
  }

  private boolean matchesGtk() {
    return platform.matches( PlatformType.GTK );
  }

  private boolean isOverlay() {
    return scrollable.getVerticalBarSize().x == 0 && scrollable.getHorizontalBarSize().y == 0;
  }
}