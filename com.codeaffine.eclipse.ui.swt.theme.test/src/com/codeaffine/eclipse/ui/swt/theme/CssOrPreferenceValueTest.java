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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.css.CSSValue;

public class CssOrPreferenceValueTest {

  private static final String PREFERENCE_VALUE = "preferenceValue";
  private static final String CSS_VALUE = "cssValue";

  private CssOrPreferenceValue cssOrPreferenceValue;
  private ScrollbarPreference preference;

  @Before
  public void setUp() {
    preference = stubPreference();
    cssOrPreferenceValue = new CssOrPreferenceValue( preference, stubCssValue() );
  }

  @Test
  public void getCssText() {
    String actual = cssOrPreferenceValue.getCssText();

    assertThat( actual ).isEqualTo( CSS_VALUE );
  }

  @Test
  public void getCssTextIfPreferenceIsCustomized() {
    when( preference.isCustomized() ).thenReturn( true );

    String actual = cssOrPreferenceValue.getCssText();

    assertThat( actual ).isEqualTo( PREFERENCE_VALUE );
  }

  @Test( expected = UnsupportedOperationException.class )
  public void setCssText() {
    cssOrPreferenceValue.setCssText( CSS_VALUE );
  }

  @Test( expected = UnsupportedOperationException.class )
  public void getCssValueType() {
    cssOrPreferenceValue.getCssValueType();
  }

  private static ScrollbarPreference stubPreference() {
    ScrollbarPreference result = mock( ScrollbarPreference.class );
    when( result.getValue() ).thenReturn( PREFERENCE_VALUE );
    return result;
  }

  private static CSSValue stubCssValue() {
    CSSValue result = mock( CSSValue.class );
    when( result.getCssText() ).thenReturn( CSS_VALUE );
    return result;
  }
}
