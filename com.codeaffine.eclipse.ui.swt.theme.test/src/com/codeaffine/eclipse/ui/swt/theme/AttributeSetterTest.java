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

import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.ADAPTER_BACKGROUND_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.ADAPTER_DEMEANOR_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.DEMEANOR_PREFERENCE_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_BACKGROUND_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_INCREMENT_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_PAGE_INCRECMENT_COLOR_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_THUMB_COLOR_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.INCREMENT_LENGTH_PREFERENCE_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.DEMEANOR_FIXED_WIDTH;
import static java.lang.Integer.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Tree;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.NonWindowsPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.Demeanor;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class AttributeSetterTest {

  @Rule public final ConditionalIgnoreRule  conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  @Test
  public void FLAT_SCROLLBAR_BACKGROUND_COLOR_SETTER() {
    ScrollbarStyle style = mock( ScrollbarStyle.class );

    FLAT_SCROLLBAR_BACKGROUND_SETTER.accept( style, expectedColor() );

    verify( style ).setBackgroundColor( expectedColor() );
  }

  @Test
  public void FLAT_SCROLLBAR_THUMB_COLOR_SETTER() {
    ScrollbarStyle style = mock( ScrollbarStyle.class );

    FLAT_SCROLLBAR_THUMB_COLOR_SETTER.accept( style, expectedColor() );

    verify( style ).setThumbColor( expectedColor() );
  }

  @Test
  public void FLAT_SCROLLBAR_PAGE_INCRECMENT_COLOR_SETTER() {
    ScrollbarStyle style = mock( ScrollbarStyle.class );

    FLAT_SCROLLBAR_PAGE_INCRECMENT_COLOR_SETTER.accept( style, expectedColor() );

    verify( style ).setPageIncrementColor( expectedColor() );
  }

  @Test
  public void FLAT_SCROLLBAR_BUTTON_LENGHT_SETTER() {
    ScrollbarStyle style = mock( ScrollbarStyle.class );

    FLAT_SCROLLBAR_INCREMENT_SETTER.accept( style, valueOf( 7 ) );

    verify( style ).setIncrementButtonLength( 7 );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void ADAPTER_BACKGROUND_SETTER() {
    Tree scrollable = new Tree( displayHelper.createShell(), SWT.NONE );
    ScrollableAdapterFactory factory = new ScrollableAdapterFactory();
    TreeAdapter style = factory.create( scrollable, TreeAdapter.class ).get();

    ADAPTER_BACKGROUND_SETTER.accept( style, expectedColor() );

    assertThat( style.getBackground() ).isEqualTo( expectedColor() );
  }

  @Test
  public void ADAPTER_DEMEANOR_SETTER() {
    ScrollbarStyle style = mock( ScrollbarStyle.class );

    ADAPTER_DEMEANOR_SETTER.accept( style, Demeanor.FIXED_SCROLL_BAR_BREADTH );

    verify( style ).setDemeanor( Demeanor.FIXED_SCROLL_BAR_BREADTH );
  }

  @Test
  public void DEMEANOR_PREFERENCE_SETTER() {
    ScrollbarStyle style = mock( ScrollbarStyle.class );
    ScrollbarPreference preference = stubPreference( DEMEANOR_FIXED_WIDTH );

    DEMEANOR_PREFERENCE_SETTER.accept( style, preference );

    verify( style ).setDemeanor( Demeanor.FIXED_SCROLL_BAR_BREADTH );
  }

  @Test
  public void INCREMENT_LENGTH_PREFERENCE_SETTER() {
    ScrollbarStyle style = mock( ScrollbarStyle.class );
    ScrollbarPreference preference = stubPreference( "8" );

    INCREMENT_LENGTH_PREFERENCE_SETTER.accept( style, preference );

    verify( style ).setIncrementButtonLength( 8 );
  }

  private Color expectedColor() {
    return displayHelper.getSystemColor( SWT.COLOR_BLACK );
  }

  private static ScrollbarPreference stubPreference( String value ) {
    ScrollbarPreference result = mock( ScrollbarPreference.class );
    when( result.getValue() ).thenReturn( value );
    return result;
  }
}