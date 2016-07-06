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

import static com.codeaffine.util.ArgumentVerification.verifyNotNull;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeKey.integerKey;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.TOP_LEVEL_WINDOW_SELECTOR;
import static java.lang.Integer.valueOf;

import java.util.function.BiConsumer;

import org.eclipse.swt.widgets.Scrollable;
import org.w3c.dom.css.CSSValue;

import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;

class N0Applicator {

  private final AttributeApplicator attributeApplicator;

  N0Applicator( ScrollableAdapterFactory factory ) {
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
      .onDefault( preserver -> attributeApplicator.apply( scrollable, intSetter, () -> parse( value, attribute ) ) )
      .onTopLevelWindow( preserver -> preserver.put( integerKey( attribute ), parse( value, attribute ) ) )
      .execute();
  }

  private static void preserve( Scrollable scrollable, CSSValue value, String attribute ) {
    String baseAttributeName = attribute.replaceAll( TOP_LEVEL_WINDOW_SELECTOR, "" );
    new AttributeApplicationOperation( scrollable, integerKey( attribute ) )
      .onDefault( preserver -> preserver.put( integerKey( attribute ), parse( value, attribute ) ) )
      .onTopLevelWindow( preserver -> preserver.put( integerKey( baseAttributeName ), parse( value, attribute ) ) )
      .execute();
  }

  static Integer parse( String value, String attribute ) {
    verifyNotNull( attribute, "attribute" );
    verifyNotNull( value, "value" );

    try {
      return checkNotNegative( valueOf( value ), attribute );
    } catch( NumberFormatException e ) {
      String message =   "Attribute '" + attribute + "' must be an int value but was "
                       + value + ".";
      throw new IllegalArgumentException( message );
    }
  }

  private static Integer checkNotNegative( Integer result, String attribute ) {
    if( result.intValue() < 0 ) {
      String message = "Attribute '" + attribute + "' must not be negative.";
      throw new IllegalArgumentException( message );
    }
    return result;
  }

  private static Integer parse( CSSValue value, String attribute ) {
    return parse( value.getCssText(), attribute );
  }
}