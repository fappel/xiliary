/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.ui.swt.theme;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSValue;

class CssOrPreferenceValue implements CSSValue {

  private final ScrollbarPreference preference;
  private final CSSValue value;

  CssOrPreferenceValue( String property, CSSValue value ) {
    this( new ScrollbarPreference( property ), value );
  }

  CssOrPreferenceValue( ScrollbarPreference preference, CSSValue value ) {
    this.preference = preference;
    this.value = value;
  }

  @Override
  public String getCssText() {
    if( preference.isCustomized() ) {
      return preference.getValue();
    }
    return value.getCssText();
  }

  @Override
  public void setCssText( String cssText ) throws DOMException {
    throw new UnsupportedOperationException();
  }

  @Override
  public short getCssValueType() {
    throw new UnsupportedOperationException();
  }
}