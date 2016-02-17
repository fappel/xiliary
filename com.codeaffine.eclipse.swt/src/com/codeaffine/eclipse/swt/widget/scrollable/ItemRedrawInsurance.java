package com.codeaffine.eclipse.swt.widget.scrollable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

class ItemRedrawInsurance {

  void register( ScrollableControl<?> scrollable ) {
    if( mustRedrawOnVerticalScrolling( scrollable ) ) {
      ensureRedrawOnVerticalScrolling( scrollable.getControl() );
    }
  }

  private static boolean mustRedrawOnVerticalScrolling( ScrollableControl<?> scrollable ) {
    return    scrollable.isOwnerDrawn()
           && scrollable.hasStyle( SWT.V_SCROLL )
           && scrollable.hasStyle( SWT.VIRTUAL );
  }

  private static void ensureRedrawOnVerticalScrolling( Scrollable scrollable ) {
    scrollable.getVerticalBar().addListener( SWT.Selection, evt -> scrollable.redraw() );
  }
}