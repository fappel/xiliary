package com.codeaffine.eclipse.ui.swt.theme;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.eclipse.swt.widgets.Scrollable;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

class AttributeApplicator {

  private static final String SCROLLABLE_STYLE = ScrollableAdapterContribution.class.getName() + "#SCROLLABLE_STYLE";

  private final ScrollableAdapterFactory factory;

  AttributeApplicator( ScrollableAdapterFactory factory ) {
    this.factory = factory;
  }

  static void attach( Scrollable scrollable, ScrollbarStyle adapterStyle ) {
    scrollable.setData( SCROLLABLE_STYLE, adapterStyle );
  }

  boolean canApply( Scrollable scrollable ) {
    return factory.isAdapted( scrollable ) && getStyleAttachment( scrollable ) != null;
  }

  <U> void apply(
    Scrollable scrollable, BiConsumer<ScrollbarStyle, U> attributeSetter, Supplier<U> valueSupplier )
  {
    ScrollbarStyle adapterStyle = getStyleAttachment( scrollable );
    applyAttribute( adapterStyle, attributeSetter, valueSupplier );
  }

  <U> void applyFromBuffer(
    Scrollable scrollable, AttributeKey<U> attribute, BiConsumer<ScrollbarStyle, U> attributeSetter )
  {
    ScrollbarStyle adapter = getStyleAttachment( scrollable );
    AttributePreserver preserver = new AttributePreserver( scrollable );
    if( preserver.get( attribute ).isPresent() && adapter != null ) {
      applyAttribute( adapter, attributeSetter, () -> preserver.get( attribute ).get() );
    }
  }

  private static <U> void applyAttribute(
    ScrollbarStyle adapterStyle, BiConsumer<ScrollbarStyle, U> attributeSetter, Supplier<U> valueSupplier )
  {
    attributeSetter.accept( adapterStyle, valueSupplier.get() );
  }

  private static ScrollbarStyle getStyleAttachment( Scrollable scrollable ) {
    return ( ScrollbarStyle )scrollable.getData( SCROLLABLE_STYLE );
  }
}