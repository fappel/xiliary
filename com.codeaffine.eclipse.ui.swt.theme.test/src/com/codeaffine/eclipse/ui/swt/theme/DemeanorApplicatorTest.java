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

import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.ADAPTER_DEMEANOR_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.CSSValueHelper.stubCssStringValue;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.ADAPTER_DEMEANOR;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.DEMEANOR_EXPAND_ON_MOUSE_OVER;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.DEMEANOR_FIXED_WIDTH;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.TOP_LEVEL_WINDOW_SELECTOR;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.css.CSSPrimitiveValue;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.Demeanor;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class DemeanorApplicatorTest {

  private static final CSSPrimitiveValue CSS_EXPAND = stubCssStringValue( DEMEANOR_EXPAND_ON_MOUSE_OVER );
  private static final CSSPrimitiveValue CSS_FIXED = stubCssStringValue( DEMEANOR_FIXED_WIDTH );

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ApplicatorTestHelper applicatorTestHelper;
  private DemeanorApplicator applicator;
  private Scrollable scrollable;
  private Shell shell;

  @Before
  public void setUp() {
    shell = displayHelper.createShell();
    scrollable = new Tree( shell, SWT.NONE );
    applicatorTestHelper = new ApplicatorTestHelper( scrollable );
    applicator = new DemeanorApplicator( applicatorTestHelper.getFactory() );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void apply() {
    ScrollbarStyle style = applicatorTestHelper.adapt();

    applicator.apply( scrollable, CSS_FIXED, ADAPTER_DEMEANOR, ADAPTER_DEMEANOR_SETTER );

    assertThat( style.getDemeanor() ).isSameAs( Demeanor.FIXED_SCROLL_BAR_BREADTH );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyTopLevelWindowAttribute() {
    String attribute = ADAPTER_DEMEANOR + TOP_LEVEL_WINDOW_SELECTOR;
    ScrollbarStyle style = applicatorTestHelper.adapt();

    applicator.apply( scrollable, CSS_FIXED, attribute, ADAPTER_DEMEANOR_SETTER );

    assertThat( style.getDemeanor() ).isSameAs( Demeanor.FIXED_SCROLL_BAR_BREADTH );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyTopLevelWindowAttributeOnChildShell() {
    String attribute = ADAPTER_DEMEANOR + TOP_LEVEL_WINDOW_SELECTOR;
    applicatorTestHelper.reparentScrollableOnChildShell();
    ScrollbarStyle style = applicatorTestHelper.adapt();

    applicator.apply( scrollable, CSS_FIXED, attribute, ADAPTER_DEMEANOR_SETTER );

    assertThat( style.getDemeanor() ).isNotSameAs( Demeanor.FIXED_SCROLL_BAR_BREADTH );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyWithBuffering() {
    applicator.apply( scrollable, CSS_FIXED, ADAPTER_DEMEANOR, ADAPTER_DEMEANOR_SETTER );
    ScrollbarStyle style = applicatorTestHelper.adapt();
    applicator.apply( scrollable, ADAPTER_DEMEANOR, ADAPTER_DEMEANOR_SETTER );

    assertThat( style.getDemeanor() ).isSameAs( Demeanor.FIXED_SCROLL_BAR_BREADTH );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyTopLevelWindowAttributeWithBuffering() {
    String attribute = ADAPTER_DEMEANOR + TOP_LEVEL_WINDOW_SELECTOR;
    applicator.apply( scrollable, CSS_FIXED, attribute, ADAPTER_DEMEANOR_SETTER );
    ScrollbarStyle style = applicatorTestHelper.adapt();
    applicator.apply( scrollable, ADAPTER_DEMEANOR, ADAPTER_DEMEANOR_SETTER );

    assertThat( style.getDemeanor() ).isSameAs( Demeanor.FIXED_SCROLL_BAR_BREADTH );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyTopLevelWindowAttributeWithBufferingOnChildShell() {
    String attribute = ADAPTER_DEMEANOR + TOP_LEVEL_WINDOW_SELECTOR;
    applicatorTestHelper.reparentScrollableOnChildShell();
    applicator.apply( scrollable, CSS_FIXED, attribute, ADAPTER_DEMEANOR_SETTER );
    ScrollbarStyle style = applicatorTestHelper.adapt();
    applicator.apply( scrollable, ADAPTER_DEMEANOR, ADAPTER_DEMEANOR_SETTER );

    assertThat( style.getDemeanor() ).isNotSameAs( Demeanor.FIXED_SCROLL_BAR_BREADTH );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyWithUnknowCSSValue() {
    applicatorTestHelper.adapt();

    Throwable actual = thrownBy( () -> {
      applicator.apply( scrollable, stubCssStringValue( "unknown" ), ADAPTER_DEMEANOR, ADAPTER_DEMEANOR_SETTER );
    } );

    assertThat( actual )
      .hasMessageContaining( "unknown" )
      .hasMessageContaining( ADAPTER_DEMEANOR )
      .hasMessageContaining( DEMEANOR_EXPAND_ON_MOUSE_OVER )
      .hasMessageContaining( DEMEANOR_FIXED_WIDTH )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyDifferentValues() {
    ScrollbarStyle style = applicatorTestHelper.adapt();

    applicator.apply( scrollable, CSS_FIXED, ADAPTER_DEMEANOR, ADAPTER_DEMEANOR_SETTER );
    applicator.apply( scrollable, CSS_EXPAND, ADAPTER_DEMEANOR, ADAPTER_DEMEANOR_SETTER );

    assertThat( style.getDemeanor() ).isSameAs( Demeanor.EXPAND_SCROLL_BAR_ON_MOUSE_OVER );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void parse() {
    Demeanor actual = DemeanorApplicator.parse( DEMEANOR_FIXED_WIDTH );

    assertThat( actual ).isSameAs( Demeanor.FIXED_SCROLL_BAR_BREADTH );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void parseWithIllegalArgument() {
    Throwable actual = thrownBy( () -> DemeanorApplicator.parse( "illegal" ) );

    assertThat( actual )
      .hasMessageContaining( "illegal" )
      .hasMessageContaining( ADAPTER_DEMEANOR )
      .hasMessageContaining( DEMEANOR_EXPAND_ON_MOUSE_OVER )
      .hasMessageContaining( DEMEANOR_FIXED_WIDTH )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void parseWithNullAsArgument() {
    Throwable actual = thrownBy( () -> DemeanorApplicator.parse( null ) );

    assertThat( actual )
      .hasMessageContaining( "value" )
      .isInstanceOf( IllegalArgumentException.class );
  }
}