package com.codeaffine.eclipse.ui.swt.theme;

import java.util.function.BiConsumer;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import com.codeaffine.eclipse.swt.widget.scrollable.Demeanor;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

class AttributeSetter {

  static final BiConsumer<ScrollbarStyle, Color> FLAT_SCROLLBAR_BACKGROUND_SETTER
    = ( style, color ) -> style.setBackgroundColor( color );
  static final BiConsumer<ScrollbarStyle, Color> FLAT_SCROLLBAR_THUMB_COLOR_SETTER
    = ( style, color ) -> style.setThumbColor( color );
  static final BiConsumer<ScrollbarStyle, Color> FLAT_SCROLLBAR_PAGE_INCRECMENT_COLOR_SETTER
    = ( style, color ) -> style.setPageIncrementColor( color );
  static final BiConsumer<ScrollbarStyle, Integer> FLAT_SCROLLBAR_INCREMENT_SETTER
    = ( style, integer ) -> style.setIncrementButtonLength( integer.intValue() );
  static final BiConsumer<ScrollbarStyle, Color> ADAPTER_BACKGROUND_SETTER
    = ( style, color ) -> ( ( Composite )style ).setBackground( color );
  static final BiConsumer<ScrollbarStyle, Demeanor> ADAPTER_DEMEANOR_SETTER
    = ( style, demeanor ) -> style.setDemeanor( demeanor );
}