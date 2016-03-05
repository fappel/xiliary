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

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.assertThat;
import static com.codeaffine.eclipse.ui.swt.theme.CSSValueHelper.stubCssBooleanValue;
import static com.codeaffine.eclipse.ui.swt.theme.CSSValueHelper.stubCssColorValue;
import static com.codeaffine.eclipse.ui.swt.theme.CSSValueHelper.stubCssIntValue;
import static com.codeaffine.eclipse.ui.swt.theme.CSSValueHelper.stubCssStringValue;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.ADAPTER_BACKGROUND;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.ADAPTER_DEMEANOR;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.DEMEANOR_EXPAND_ON_MOUSE_OVER;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.DEMEANOR_FIXED_WIDTH;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_BACKGROUND;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_INCREMENT_LENGTH;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_PAGE_INCREMENT;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_THUMB;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.SUPPORTED_ADAPTERS;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.TOP_LEVEL_WINDOW_SELECTOR;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.e4.ui.css.swt.helpers.CSSSWTColorHelper.getRGBA;

import java.lang.reflect.Constructor;

import org.eclipse.e4.ui.css.swt.dom.ControlElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.css.CSSPrimitiveValue;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.RegistryAdapter;
import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.NonWindowsPlatform;
import com.codeaffine.eclipse.swt.widget.scrollable.Demeanor;
import com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapter;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

@SuppressWarnings("restriction")
@RunWith( Parameterized.class )
public class ScrollableAdapterContributionPDETest {

  private static final String NAME = "name";
  private static final CSSPrimitiveValue PAGE_INC = stubCssColorValue( 20, 75, 230 );
  private static final CSSPrimitiveValue BACK_GROUND = stubCssColorValue( 10, 100, 200 );
  private static final CSSPrimitiveValue THUMB = stubCssColorValue( 15, 150, 250 );
  private static final CSSPrimitiveValue TRUE = stubCssBooleanValue( true );
  private static final CSSPrimitiveValue FALSE = stubCssBooleanValue( false );
  private static final CSSPrimitiveValue FIXED_WIDTH = stubCssStringValue( DEMEANOR_FIXED_WIDTH );
  private static final CSSPrimitiveValue EXPAND = stubCssStringValue( DEMEANOR_EXPAND_ON_MOUSE_OVER );
  private static final CSSPrimitiveValue INC_LENGTH = stubCssIntValue( 7 );
  private static final CSSPrimitiveValue INC_ZERO = stubCssIntValue( 0 );

  @Rule public final ScrollbarPreferenceRule scrollbarPreferenceRule = new ScrollbarPreferenceRule();
  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  @Parameter
  public TypePair<?, ?> typePair;

  private ScrollableAdapterContribution contribution;
  private Shell shell;

  @Parameters
  public static Object data() {
    return SUPPORTED_ADAPTERS;
  }

  @Before
  public void setUp() {
    shell = displayHelper.createShell();
    contribution = readContribution();
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void propertyHandlerContribution() {
    Extension actual = new RegistryAdapter()
      .readExtension( "org.eclipse.e4.ui.css.core.propertyHandler" )
      .thatMatches( attribute( "handler", "com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution" ) )
      .process();

    assertThat( actual )
      .hasAttributeValue( "adapter", "org.eclipse.e4.ui.css.swt.dom.WidgetElement" )
      .hasAttributeValue( "composite", "false" )
      .hasChildWithAttributeValue( NAME, FLAT_SCROLL_BAR )
      .hasChildWithAttributeValue( NAME, FLAT_SCROLL_BAR_BACKGROUND )
      .hasChildWithAttributeValue( NAME, FLAT_SCROLL_BAR_THUMB )
      .hasChildWithAttributeValue( NAME, FLAT_SCROLL_BAR_PAGE_INCREMENT )
      .hasChildWithAttributeValue( NAME, FLAT_SCROLL_BAR_INCREMENT_LENGTH )
      .hasChildWithAttributeValue( NAME, ADAPTER_BACKGROUND )
      .hasChildWithAttributeValue( NAME, ADAPTER_DEMEANOR )
      .hasChildWithAttributeValue( NAME, FLAT_SCROLL_BAR_BACKGROUND + TOP_LEVEL_WINDOW_SELECTOR )
      .hasChildWithAttributeValue( NAME, FLAT_SCROLL_BAR_THUMB + TOP_LEVEL_WINDOW_SELECTOR )
      .hasChildWithAttributeValue( NAME, FLAT_SCROLL_BAR_PAGE_INCREMENT + TOP_LEVEL_WINDOW_SELECTOR )
      .hasChildWithAttributeValue( NAME, FLAT_SCROLL_BAR_INCREMENT_LENGTH + TOP_LEVEL_WINDOW_SELECTOR )
      .hasChildWithAttributeValue( NAME, ADAPTER_BACKGROUND + TOP_LEVEL_WINDOW_SELECTOR )
      .hasChildWithAttributeValue( NAME, ADAPTER_DEMEANOR + TOP_LEVEL_WINDOW_SELECTOR );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyFlatScrollbar() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyFlatScrollbarOnNonControlElement() throws Exception {
    Control control = new Label( shell, SWT.NONE );

    contribution.applyCSSProperty( newElement( control ), FLAT_SCROLL_BAR, null, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isSameAs( control );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyFlatScrollbarOnScrollableAdapter() throws Exception {
    ScrollableAdapter<?> control = new FlatScrollBarTree( shell, composite -> new Tree( composite, SWT.NONE ) );

    contribution.applyCSSProperty( newElement( control.getScrollable() ), FLAT_SCROLL_BAR, TRUE, null, null );
    contribution.applyCSSProperty( newElement( control.getScrollable() ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );

    assertThat( control.getChildren()[ 1 ] ).isNotInstanceOf( TreeAdapter.class );
    assertThat( ( ( ScrollbarStyle )control ).getThumbColor() ).isNotEqualTo( expectedColor( THUMB ) );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyFlatScrollbarOnNonAdaptableType() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyOfSchemeAfterFlatScrollbar() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), ADAPTER_BACKGROUND, BACK_GROUND, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_BACKGROUND, BACK_GROUND, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_PAGE_INCREMENT, PAGE_INC, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_INCREMENT_LENGTH, INC_LENGTH, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), ADAPTER_DEMEANOR, FIXED_WIDTH, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
    assertThat( shell.getChildren()[ 0 ].getBackground() ).isEqualTo( expectedColor( BACK_GROUND ) );
    assertThat( getAdapterStyle().getBackgroundColor() ).isEqualTo( expectedColor( BACK_GROUND ) );
    assertThat( getAdapterStyle().getThumbColor() ).isEqualTo( expectedColor( THUMB ) );
    assertThat( getAdapterStyle().getPageIncrementColor() ).isEqualTo( expectedColor( PAGE_INC ) );
    assertThat( getAdapterStyle().getDemeanor() ).isSameAs( Demeanor.FIXED_SCROLL_BAR_BREADTH );
    assertThat( getAdapterStyle().getIncrementButtonLength() ).isEqualTo( expectedInt( INC_LENGTH ) );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyPreferenceOnFlatScrollbarAdaption() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );
    scrollbarPreferenceRule.setValue( FLAT_SCROLL_BAR_INCREMENT_LENGTH, INC_LENGTH.getCssText() );
    scrollbarPreferenceRule.setValue( ADAPTER_DEMEANOR, DEMEANOR_FIXED_WIDTH );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_INCREMENT_LENGTH, INC_ZERO, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), ADAPTER_DEMEANOR, EXPAND, null, null );

    assertThat( getAdapterStyle().getIncrementButtonLength() ).isEqualTo( expectedInt( INC_LENGTH ) );
    assertThat( getAdapterStyle().getDemeanor() ).isSameAs( Demeanor.FIXED_SCROLL_BAR_BREADTH );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyFlatScrollbarAfterScheme() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_BACKGROUND, BACK_GROUND, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_PAGE_INCREMENT, PAGE_INC, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_INCREMENT_LENGTH, INC_LENGTH, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), ADAPTER_BACKGROUND, BACK_GROUND, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), ADAPTER_DEMEANOR, FIXED_WIDTH, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
    assertThat( shell.getChildren()[ 0 ].getBackground() ).isEqualTo( expectedColor( BACK_GROUND ) );
    assertThat( getAdapterStyle().getBackgroundColor() ).isEqualTo( expectedColor( BACK_GROUND ) );
    assertThat( getAdapterStyle().getThumbColor() ).isEqualTo( expectedColor( THUMB ) );
    assertThat( getAdapterStyle().getPageIncrementColor() ).isEqualTo( expectedColor( PAGE_INC ) );
    assertThat( getAdapterStyle().getDemeanor() ).isEqualTo( Demeanor.FIXED_SCROLL_BAR_BREADTH );
    assertThat( getAdapterStyle().getIncrementButtonLength() ).isEqualTo( expectedInt( INC_LENGTH ) );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyPreferenceOnFlatScrollbarAfterScheme() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );
    scrollbarPreferenceRule.setValue( FLAT_SCROLL_BAR_INCREMENT_LENGTH, INC_LENGTH.getCssText() );
    scrollbarPreferenceRule.setValue( ADAPTER_DEMEANOR, DEMEANOR_FIXED_WIDTH );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_INCREMENT_LENGTH, INC_ZERO, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), ADAPTER_DEMEANOR, EXPAND, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( getAdapterStyle().getDemeanor() ).isEqualTo( Demeanor.FIXED_SCROLL_BAR_BREADTH );
    assertThat( getAdapterStyle().getIncrementButtonLength() ).isEqualTo( expectedInt( INC_LENGTH ) );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyFlatScrollbarAfterSchemeIfMarkedAdapted() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );
    new ScrollableAdapterFactory().markAdapted( scrollable );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isInstanceOf( typePair.scrollableType );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyFlatScrollbarWithFalseAsCssValue() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, FALSE, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isSameAs( scrollable );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyFlatScrollbarWithWrongCssValueType() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, stubCssStringValue( "bad" ), null, null );

    assertThat( shell.getChildren()[ 0 ] ).isSameAs( scrollable );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyOfThumbWithAlternateTopLevelWindowSelectorOnTopLevelShellAfterFlatScrollbar()
    throws Exception
  {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );
    CSSPrimitiveValue colorValue = stubCssColorValue( 123, 22, 213 );
    String thumbSelectorForTopLevelWindow = FLAT_SCROLL_BAR_THUMB + TOP_LEVEL_WINDOW_SELECTOR;

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), thumbSelectorForTopLevelWindow, colorValue, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
    assertThat( getAdapterStyle().getThumbColor() ).isEqualTo( expectedColor( colorValue ) );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyFlatScrollbarThumbWithAlternateTopLevelWindowSelectorOnTopLevelShell()
    throws Exception
  {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );
    CSSPrimitiveValue colorValue = stubCssColorValue( 123, 22, 213 );
    String thumbSelectorForTopLevelWindow = FLAT_SCROLL_BAR_THUMB + TOP_LEVEL_WINDOW_SELECTOR;

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), thumbSelectorForTopLevelWindow, colorValue, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
    assertThat( getAdapterStyle().getThumbColor() ).isEqualTo( expectedColor( colorValue ) );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyOfThumbWithAlternateTopLevelWindowSelectorOnTopLevelShellAndReverseSettingAfterFlatBar()
    throws Exception
  {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );
    CSSPrimitiveValue colorValue = stubCssColorValue( 123, 22, 213 );
    String thumbSelectorForTopLevelWindow = FLAT_SCROLL_BAR_THUMB + TOP_LEVEL_WINDOW_SELECTOR;

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), thumbSelectorForTopLevelWindow, colorValue, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
    assertThat( getAdapterStyle().getThumbColor() ).isEqualTo( expectedColor( colorValue ) );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyFlatScrollbarThumbWithAlternateTopLevelWindowSelectorOnTopLevelShellAndReverseSetting()
    throws Exception
  {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );
    CSSPrimitiveValue colorValue = stubCssColorValue( 123, 22, 213 );
    String thumbSelectorForTopLevelWindow = FLAT_SCROLL_BAR_THUMB + TOP_LEVEL_WINDOW_SELECTOR;

    contribution.applyCSSProperty( newElement( scrollable ), thumbSelectorForTopLevelWindow, colorValue, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
    assertThat( getAdapterStyle().getThumbColor() ).isEqualTo( expectedColor( colorValue ) );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyOfThumbWithAlternateTopLevelWindowSelectorOnChildShellAfterFlatScrollbar()
    throws Exception
  {
    Shell child = new Shell( shell );
    Scrollable scrollable = createScrollable( child, typePair.scrollableType );
    CSSPrimitiveValue colorValue = stubCssColorValue( 123, 22, 213 );
    String thumbSelectorForTopLevelWindow = FLAT_SCROLL_BAR_THUMB + TOP_LEVEL_WINDOW_SELECTOR;

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), thumbSelectorForTopLevelWindow, colorValue, null, null );

    assertThat( child.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
    assertThat( ( ( ScrollbarStyle )child.getChildren()[ 0 ] ).getThumbColor() ).isEqualTo( expectedColor( THUMB ) );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyOfThumbWithAlternateTopLevelWindowSelectorOnChildShellWithReverseSettingAfterFlatBar()
    throws Exception
  {
    Shell child = new Shell( shell );
    Scrollable scrollable = createScrollable( child, typePair.scrollableType );
    CSSPrimitiveValue colorValue = stubCssColorValue( 123, 22, 213 );
    String thumbSelectorForTopLevelWindow = FLAT_SCROLL_BAR_THUMB + TOP_LEVEL_WINDOW_SELECTOR;

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), thumbSelectorForTopLevelWindow, colorValue, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );

    assertThat( child.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
    assertThat( ( ( ScrollbarStyle )child.getChildren()[ 0 ] ).getThumbColor() ).isEqualTo( expectedColor( THUMB ) );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyFlatScrollbarThumbWithAlternateTopLevelWindowSelectorOnChildShellWith()
    throws Exception
  {
    Shell child = new Shell( shell );
    Scrollable scrollable = createScrollable( child, typePair.scrollableType );
    CSSPrimitiveValue colorValue = stubCssColorValue( 123, 22, 213 );
    String thumbSelectorForTopLevelWindow = FLAT_SCROLL_BAR_THUMB + TOP_LEVEL_WINDOW_SELECTOR;

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), thumbSelectorForTopLevelWindow, colorValue, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( child.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
    assertThat( ( ( ScrollbarStyle )child.getChildren()[ 0 ] ).getThumbColor() ).isEqualTo( expectedColor( THUMB ) );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCSSPropertyFlatScrollbarThumbWithAlternateTopLevelWindowSelectorOnChildShellWithReverseSetting()
    throws Exception
  {
    Shell child = new Shell( shell );
    Scrollable scrollable = createScrollable( child, typePair.scrollableType );
    CSSPrimitiveValue colorValue = stubCssColorValue( 123, 22, 213 );
    String thumbSelectorForTopLevelWindow = FLAT_SCROLL_BAR_THUMB + TOP_LEVEL_WINDOW_SELECTOR;

    contribution.applyCSSProperty( newElement( scrollable ), thumbSelectorForTopLevelWindow, colorValue, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( child.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
    assertThat( ( ( ScrollbarStyle )child.getChildren()[ 0 ] ).getThumbColor() ).isEqualTo( expectedColor( THUMB ) );
  }

  @Test
  @ConditionalIgnore( condition = NonWindowsPlatform.class )
  public void applyCssPropertyWithUnsupportedAttribute() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );

    Throwable actual
      = thrownBy( () -> contribution.applyCSSProperty( newElement( scrollable ), "unsupported", TRUE, null, null ) );

    assertThat( actual )
      .hasMessageContaining( "unsupported" )
      .isInstanceOf( IllegalArgumentException.class );
  }

  private static ScrollableAdapterContribution readContribution() {
    return new RegistryAdapter()
      .createExecutableExtension( "org.eclipse.e4.ui.css.core.propertyHandler", ScrollableAdapterContribution.class )
      .withTypeAttribute( "handler" )
      .thatMatches( attribute( "handler", "com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution" ) )
      .process();
  }

  private static ControlElement newElement( Control control ) {
    return new ControlElement( control, null );
  }

  private static Scrollable createScrollable( Composite composite, Class<?> type ) throws Exception {
    Constructor<?> constructor = type.getDeclaredConstructor( Composite.class, int.class );
    return ( Scrollable )constructor.newInstance( composite, SWT.V_SCROLL | SWT.H_SCROLL );
  }

  private ScrollbarStyle getAdapterStyle() {
    return ( ScrollbarStyle )shell.getChildren()[ 0 ];
  }

  private Color expectedColor( CSSPrimitiveValue value ) {
    return new Color( displayHelper.getDisplay(), getRGBA( value ).rgb );
  }

  private static int expectedInt( CSSPrimitiveValue intValue ) {
    return Integer.parseInt( intValue.getCssText() );
  }

}