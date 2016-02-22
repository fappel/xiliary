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

import static com.codeaffine.eclipse.swt.widget.scrollable.Demeanor.EXPAND_SCROLL_BAR_ON_MOUSE_OVER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeKey.demeanorKey;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeKey.integerKey;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.ADAPTER_DEMEANOR_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.DEMEANOR_PREFERENCE_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_INCREMENT_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.INCREMENT_LENGTH_PREFERENCE_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.ADAPTER_DEMEANOR;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_INCREMENT_LENGTH;
import static java.lang.Integer.valueOf;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapter;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory.Adapter;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

class ScrollbarPreferenceApplicator implements IPropertyChangeListener {

  private final ScrollbarPreference incrementButtonLengthPreference;
  private final ScrollbarPreference demeanorPreference;
  private final DemeanorApplicator demeanorApplicator;
  private final ScrollbarStyleCollector collector;
  private final N0Applicator n0Applicator;

  ScrollbarPreferenceApplicator() {
    incrementButtonLengthPreference = new ScrollbarPreference( FLAT_SCROLL_BAR_INCREMENT_LENGTH );
    demeanorPreference = new ScrollbarPreference( ADAPTER_DEMEANOR );
    n0Applicator = new N0Applicator( new ScrollableAdapterFactory() );
    demeanorApplicator = new DemeanorApplicator( new ScrollableAdapterFactory() );
    collector = new ScrollbarStyleCollector();
  }

  @Override
  public void propertyChange( PropertyChangeEvent event ) {
    switch( event.getProperty() ) {
      case ADAPTER_DEMEANOR:
        collector.collect().forEach( style -> applyDemeanor( style ) );
        break;
      case FLAT_SCROLL_BAR_INCREMENT_LENGTH:
        collector.collect().forEach( style -> applyIncrementButtonLength( style ) );
        break;
    }
  }

  private void applyDemeanor( ScrollbarStyle style ) {
    apply( style,
           demeanorPreference,
           DEMEANOR_PREFERENCE_SETTER,
           demeanorKey( ADAPTER_DEMEANOR ),
           ADAPTER_DEMEANOR_SETTER,
           scrollable -> demeanorApplicator.apply( scrollable, ADAPTER_DEMEANOR, ADAPTER_DEMEANOR_SETTER ),
           EXPAND_SCROLL_BAR_ON_MOUSE_OVER );
  }

  private void applyIncrementButtonLength( ScrollbarStyle style ) {
    BiConsumer<ScrollbarStyle, Integer> attributeSetter = FLAT_SCROLLBAR_INCREMENT_SETTER;
    apply( style,
           incrementButtonLengthPreference,
           INCREMENT_LENGTH_PREFERENCE_SETTER,
           integerKey( FLAT_SCROLL_BAR_INCREMENT_LENGTH ),
           attributeSetter,
           scrollable -> n0Applicator.apply( scrollable, FLAT_SCROLL_BAR_INCREMENT_LENGTH, attributeSetter ),
           valueOf( 0 ) );
  }

  private static <T> void apply(
    ScrollbarStyle style,
    ScrollbarPreference preference,
    BiConsumer<ScrollbarStyle, ScrollbarPreference> preferenceSetter,
    AttributeKey<?> attributeKey,
    BiConsumer<ScrollbarStyle, T> attributeSetter,
    Consumer<Scrollable> styleApplicator,
    T defaultValue )
  {
    preference.apply( style, preferenceSetter );
    if( !preference.isCustomized() ) {
      applyCssStyle( style, attributeKey, attributeSetter, styleApplicator, defaultValue );
    }
    ( ( Composite )style ).layout();
  }

  private static <T> void applyCssStyle(
    ScrollbarStyle style,
    AttributeKey<?> attributeKey,
    BiConsumer<ScrollbarStyle, T> attributeSetter,
    Consumer<Scrollable> styleApplicator,
    T defaultValue )
  {
    Scrollable scrollable = getScrollable( style );
    if( hasPreservedCssValue( attributeKey, scrollable ) ) {
      styleApplicator.accept( scrollable );
    } else {
      attributeSetter.accept( style, defaultValue );
    }
  }

  private static Scrollable getScrollable( ScrollbarStyle style ) {
    if( style instanceof Adapter ) {
      return ( ( Adapter<?> )style ).getScrollable();
    }
    return ( ( ScrollableAdapter<?> )style ).getScrollable();
  }

  private static boolean hasPreservedCssValue( AttributeKey<?> attributeKey, Scrollable scrollable ) {
    return new AttributePreserver( scrollable ).get( attributeKey ).isPresent();
  }
}