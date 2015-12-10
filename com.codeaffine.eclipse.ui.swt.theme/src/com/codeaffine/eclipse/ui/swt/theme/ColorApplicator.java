package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.AttributeKey.colorKey;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.TOP_LEVEL_WINDOW_SELECTOR;
import static org.eclipse.e4.ui.css.swt.helpers.CSSSWTColorHelper.getRGBA;

import java.util.function.BiConsumer;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Scrollable;
import org.w3c.dom.css.CSSValue;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

class ColorApplicator {

  private final AttributeApplicator attributeApplicator;
  private final ColorRegistry colorRegistry;

  ColorApplicator( ScrollableAdapterFactory factory ) {
    attributeApplicator = new AttributeApplicator( factory );
    colorRegistry = new ColorRegistry();
  }

  void apply( Scrollable scrollable, String attribute, BiConsumer<ScrollbarStyle, Color> colorSetter ) {
    attributeApplicator.applyFromBuffer( scrollable, colorKey( attribute ), colorSetter );
  }

  void apply( Scrollable scrollable, CSSValue value, String attribute, BiConsumer<ScrollbarStyle, Color> colorSetter ) {
    if( attributeApplicator.canApply( scrollable ) ) {
      doApply( scrollable, value, attribute, colorSetter );
    } else {
      preserve( scrollable, value, attribute );
    }
  }

  private void doApply(
    Scrollable scrollable, CSSValue value, String attribute, BiConsumer<ScrollbarStyle, Color> colorSetter )
  {
    new AttributeApplicationOperation( scrollable, colorKey( attribute ) )
      .onDefault( preserver -> attributeApplicator.apply( scrollable, colorSetter, () -> getColor( value ) ) )
      .onTopLevelWindow( preserver -> preserver.put( colorKey( attribute ), getColor( value ) ) )
      .execute();
  }

  private void preserve( Scrollable scrollable, CSSValue value, String attribute ) {
    String baseAttributeName = attribute.replaceAll( TOP_LEVEL_WINDOW_SELECTOR, "" );
    new AttributeApplicationOperation( scrollable, colorKey( attribute ) )
      .onDefault( preserver -> preserver.put( colorKey( attribute ), getColor( value ) ) )
      .onTopLevelWindow( preserver -> preserver.put( colorKey( baseAttributeName ), getColor( value ) ) )
      .execute();
  }

  @SuppressWarnings("restriction")
  private Color getColor( CSSValue value ) {
    String key = value.getCssText();
    if( !colorRegistry.hasValueFor( key ) ) {
      colorRegistry.put( key, getRGBA( value ).rgb );
    }
    return colorRegistry.get( key );
  }
}