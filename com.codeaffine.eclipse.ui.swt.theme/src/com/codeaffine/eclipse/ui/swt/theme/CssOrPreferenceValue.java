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