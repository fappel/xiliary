package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.assertThat;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_BACKGROUND;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_PAGE_INCREMENT;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_THUMB;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.SUPPORTED_ADAPTERS;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.TOP_LEVEL_WINDOW_SELECTOR;
import static java.lang.Integer.toHexString;
import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.e4.ui.css.swt.helpers.CSSSWTColorHelper.getRGBA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.w3c.dom.css.CSSPrimitiveValue.CSS_RGBCOLOR;
import static org.w3c.dom.css.CSSValue.CSS_PRIMITIVE_VALUE;

import java.lang.reflect.Constructor;

import org.eclipse.e4.ui.css.core.css2.CSS2RGBColorImpl;
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
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.GtkPlatform;
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

  private static final CSSPrimitiveValue PAGE_INC = stubCssColorValue( 20, 75, 230 );
  private static final CSSPrimitiveValue BACK_GROUND = stubCssColorValue( 10, 100, 200 );
  private static final CSSPrimitiveValue THUMB = stubCssColorValue( 15, 150, 250 );
  private static final CSSPrimitiveValue TRUE = stubCccBooleanValue( true );
  private static final CSSPrimitiveValue FALSE = stubCccBooleanValue( false );

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
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void propertyHandlerContribution() {
    Extension actual = new RegistryAdapter()
      .readExtension( "org.eclipse.e4.ui.css.core.propertyHandler" )
      .thatMatches( attribute( "handler", "com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution" ) )
      .process();

    assertThat( actual )
      .hasAttributeValue( "adapter", "org.eclipse.e4.ui.css.swt.dom.WidgetElement" )
      .hasAttributeValue( "composite", "false" )
      .hasChildWithAttributeValue( "name", FLAT_SCROLL_BAR )
      .hasChildWithAttributeValue( "name", FLAT_SCROLL_BAR_BACKGROUND )
      .hasChildWithAttributeValue( "name", FLAT_SCROLL_BAR_THUMB )
      .hasChildWithAttributeValue( "name", FLAT_SCROLL_BAR_PAGE_INCREMENT )
      .hasChildWithAttributeValue( "name", FLAT_SCROLL_BAR_BACKGROUND + TOP_LEVEL_WINDOW_SELECTOR )
      .hasChildWithAttributeValue( "name", FLAT_SCROLL_BAR_THUMB + TOP_LEVEL_WINDOW_SELECTOR )
      .hasChildWithAttributeValue( "name", FLAT_SCROLL_BAR_PAGE_INCREMENT + TOP_LEVEL_WINDOW_SELECTOR );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyCSSPropertyFlatScrollbar() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyCSSPropertyFlatScrollbarOnNonControlElement() throws Exception {
    Control control = new Label( shell, SWT.NONE );

    contribution.applyCSSProperty( newElement( control ), FLAT_SCROLL_BAR, null, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isSameAs( control );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyCSSPropertyFlatScrollbarOnScrollableAdapter() throws Exception {
    ScrollableAdapter<?> control = new FlatScrollBarTree( shell, composite -> new Tree( composite, SWT.NONE ) );

    contribution.applyCSSProperty( newElement( control.getScrollable() ), FLAT_SCROLL_BAR, TRUE, null, null );
    contribution.applyCSSProperty( newElement( control.getScrollable() ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );

    assertThat( control.getChildren()[ 1 ] ).isNotInstanceOf( TreeAdapter.class );
    assertThat( ( ( ScrollbarStyle )control ).getThumbColor() ).isNotEqualTo( expectedColor( THUMB ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyCSSPropertyFlatScrollbarOnNonAdaptableType() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyCSSPropertyOfColorSchemeAfterFlatScrollbar() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_BACKGROUND, BACK_GROUND, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_PAGE_INCREMENT, PAGE_INC, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
    assertThat( getAdapterStyle().getBackgroundColor() ).isEqualTo( expectedColor( BACK_GROUND ) );
    assertThat( getAdapterStyle().getThumbColor() ).isEqualTo( expectedColor( THUMB ) );
    assertThat( getAdapterStyle().getPageIncrementColor() ).isEqualTo( expectedColor( PAGE_INC ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyCSSPropertyFlatScrollbarAfterColorScheme() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_BACKGROUND, BACK_GROUND, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_PAGE_INCREMENT, PAGE_INC, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isInstanceOf( typePair.adapterType );
    assertThat( getAdapterStyle().getBackgroundColor() ).isEqualTo( expectedColor( BACK_GROUND ) );
    assertThat( getAdapterStyle().getThumbColor() ).isEqualTo( expectedColor( THUMB ) );
    assertThat( getAdapterStyle().getPageIncrementColor() ).isEqualTo( expectedColor( PAGE_INC ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyCSSPropertyFlatScrollbarAfterColorSchemeIfMarkedAdapted() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );
    new ScrollableAdapterFactory().markAdapted( scrollable );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR_THUMB, THUMB, null, null );
    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isInstanceOf( typePair.scrollableType );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyCSSPropertyFlatScrollbarWithFalseAsCssValue() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, FALSE, null, null );

    assertThat( shell.getChildren()[ 0 ] ).isSameAs( scrollable );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyCSSPropertyFlatScrollbarWithWrongCssValueType() throws Exception {
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, stubCssStringValue( "bad" ), null, null );

    assertThat( shell.getChildren()[ 0 ] ).isSameAs( scrollable );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
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
  @ConditionalIgnore( condition = GtkPlatform.class )
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
  @ConditionalIgnore( condition = GtkPlatform.class )
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
  @ConditionalIgnore( condition = GtkPlatform.class )
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
  @ConditionalIgnore( condition = GtkPlatform.class )
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
  @ConditionalIgnore( condition = GtkPlatform.class )
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
  @ConditionalIgnore( condition = GtkPlatform.class )
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
  @ConditionalIgnore( condition = GtkPlatform.class )
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
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyBackgroundColorOnTopLevelWindowWorkaround() throws Exception {
    Color expectedBackground = displayHelper.getDisplay().getSystemColor( SWT.COLOR_BLUE );
    shell.setBackground( expectedBackground );
    Scrollable scrollable = createScrollable( shell, typePair.scrollableType );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( shell.getChildren()[ 0 ].getBackground() ).isEqualTo( expectedBackground );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyBackgroundColorOnNonTopLevelWindowWorkaround() throws Exception {
    Color expectedBackground = displayHelper.getDisplay().getSystemColor( SWT.COLOR_BLUE );
    shell.setBackground( expectedBackground );
    Shell childShell = new Shell( shell );
    Scrollable scrollable = createScrollable( childShell, typePair.scrollableType );

    contribution.applyCSSProperty( newElement( scrollable ), FLAT_SCROLL_BAR, TRUE, null, null );

    assertThat( childShell.getChildren()[ 0 ].getBackground() ).isNotEqualTo( expectedBackground );
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
    return ( Scrollable )constructor.newInstance( composite, SWT.NONE );
  }

  private static CSSPrimitiveValue stubCssColorValue( int red, int green, int blue ) {
    CSSPrimitiveValue result = mock( CSSPrimitiveValue.class );
    CSS2RGBColorImpl value = new CSS2RGBColorImpl( red, green, blue );
    when( result.getCssValueType() ).thenReturn( CSS_PRIMITIVE_VALUE );
    when( result.getPrimitiveType() ).thenReturn( CSS_RGBCOLOR );
    when( result.getRGBColorValue() ).thenReturn( value );
    when( result.getCssText() ).thenReturn( "#" + toHexString( red ) + toHexString( green ) + toHexString( blue ) );
    return result;
  }

  private static CSSPrimitiveValue stubCccBooleanValue( boolean booleanValue ) {
    return stubCssStringValue( valueOf( booleanValue ) );
  }

  private static CSSPrimitiveValue stubCssStringValue( String stringValue ) {
    CSSPrimitiveValue result = mock( CSSPrimitiveValue.class );
    when( result.getCssText() ).thenReturn( stringValue );
    return result;
  }

  private ScrollbarStyle getAdapterStyle() {
    return ( ScrollbarStyle )shell.getChildren()[ 0 ];
  }

  private Color expectedColor( CSSPrimitiveValue value ) {
    return new Color( displayHelper.getDisplay(), getRGBA( value ).rgb );
  }
}