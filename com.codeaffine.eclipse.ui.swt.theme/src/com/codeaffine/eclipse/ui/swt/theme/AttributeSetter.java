/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_PAGE_INCREMENT;

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
  static final BiConsumer<ScrollbarStyle, ScrollbarPreference> DEMEANOR_PREFERENCE_SETTER
    = ( style, preference ) -> style.setDemeanor( DemeanorApplicator.parse( preference.getValue() ) );
  static final BiConsumer<ScrollbarStyle, ScrollbarPreference> INCREMENT_LENGTH_PREFERENCE_SETTER
    = ( style, preference ) -> style.setIncrementButtonLength(
        N0Applicator.parse( preference.getValue(), FLAT_SCROLL_BAR_PAGE_INCREMENT ).intValue() );
}