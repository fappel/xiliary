/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.ControlElements.extractControl;
import static com.codeaffine.eclipse.ui.swt.theme.ControlElements.isControlElement;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.eclipse.e4.ui.css.swt.helpers.CSSSWTColorHelper.getRGBA;

import java.util.List;

import org.eclipse.e4.ui.css.core.dom.properties.ICSSPropertyHandler;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.css.CSSValue;

@SuppressWarnings("restriction")
public class TopLevelWindowAttributeContribution implements ICSSPropertyHandler {

  public static final String TOP_LEVEL_WINDOW_ELEMENT_PREFIX = "top-level-window-element-";
  public static final String BACKGROUND_COLOR = TOP_LEVEL_WINDOW_ELEMENT_PREFIX + "background-color";

  private final static List<String> PROPERTY_NAMES = unmodifiableList( asList( new String[] {
    BACKGROUND_COLOR
  } ) );

  private final ColorRegistry colorRegistry;

  public TopLevelWindowAttributeContribution() {
    colorRegistry = new ColorRegistry();
  }

  @Override
  public String retrieveCSSProperty( Object element, String property, String pseudo, CSSEngine engine )
    throws Exception
  {
    return null;
  }

  @Override
  public boolean applyCSSProperty( Object element, String property, CSSValue value, String pseudo, CSSEngine engine )
      throws Exception
  {
    if( mustApply( element, property ) ) {
      doApplyCssProperty( extractControl( element ), property, value );
    }
    return false;
  }

  private static boolean mustApply( Object element, String property ) {
    return    isControlElement( element )
           && isTopLevelWindowElement( extractControl( element ) )
           && PROPERTY_NAMES.contains( property );
  }

  private static boolean isTopLevelWindowElement( Control element ) {
    return element.getShell().getParent() == null;
  }

  private void doApplyCssProperty( Control element, String property, CSSValue value ) {
    switch( property ) {
      case BACKGROUND_COLOR:
        element.setBackground( getColor( value ) );
        break;
    }
  }

  Color getColor( CSSValue value ) {
    String key = value.getCssText();
    if( !colorRegistry.hasValueFor( key ) ) {
      colorRegistry.put( key, getRGBA( value ).rgb );
    }
    return colorRegistry.get( key );
  }
}
