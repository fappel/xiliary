package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.AttributeKey.integerKey;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_INCREMENT_LENGTH;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.TOP_LEVEL_WINDOW_SELECTOR;
import static java.lang.Integer.valueOf;

import java.util.function.BiConsumer;

import org.eclipse.swt.widgets.Scrollable;
import org.w3c.dom.css.CSSValue;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

class IntApplicator {

  private final AttributeApplicator attributeApplicator;

  IntApplicator( ScrollableAdapterFactory factory ) {
    attributeApplicator = new AttributeApplicator( factory );
  }

  void apply( Scrollable scrollable, String attribute, BiConsumer<ScrollbarStyle, Integer> intSetter ) {
    attributeApplicator.applyFromBuffer( scrollable, integerKey( attribute ), intSetter );
  }

  void apply( Scrollable scrollable, CSSValue value, String attribute, BiConsumer<ScrollbarStyle, Integer> intSetter ) {
    if( attributeApplicator.canApply( scrollable ) ) {
      doApply( scrollable, value, attribute, intSetter );
    } else {
      preserve( scrollable, value, attribute );
    }
  }

  private void doApply(
    Scrollable scrollable, CSSValue value, String attribute, BiConsumer<ScrollbarStyle, Integer> intSetter )
  {
    new AttributeApplicationOperation( scrollable, integerKey( attribute ) )
      .onDefault( preserver -> attributeApplicator.apply( scrollable, intSetter, () -> getInteger( value ) ) )
      .onTopLevelWindow( preserver -> preserver.put( integerKey( attribute ), getInteger( value ) ) )
      .execute();
  }

  private static void preserve( Scrollable scrollable, CSSValue value, String attribute ) {
    String baseAttributeName = attribute.replaceAll( TOP_LEVEL_WINDOW_SELECTOR, "" );
    new AttributeApplicationOperation( scrollable, integerKey( attribute ) )
      .onDefault( preserver -> preserver.put( integerKey( attribute ), getInteger( value ) ) )
      .onTopLevelWindow( preserver -> preserver.put( integerKey( baseAttributeName ), getInteger( value ) ) )
      .execute();
  }

  private static Integer getInteger( CSSValue value ) {
    Integer result = parse( value );
    checkNotNegative( result );
    return result;
  }

  private static Integer parse( CSSValue value ) {
    try {
      return valueOf( value.getCssText() );
    } catch( NumberFormatException e ) {
      String message =   "Attribute '" + FLAT_SCROLL_BAR_INCREMENT_LENGTH + "' must be an int value but was "
                        + value.getCssText() + ".";
      throw new IllegalArgumentException( message );
    }
  }

  private static void checkNotNegative( Integer result ) {
    if( result.intValue() < 0 ) {
      String message = "Attribute '" + FLAT_SCROLL_BAR_INCREMENT_LENGTH + "' must not be negative.";
      throw new IllegalArgumentException( message );
    }
  }
}