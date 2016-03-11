package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static com.codeaffine.eclipse.core.runtime.test.util.ExtensionAssert.assertThat;
import static com.codeaffine.eclipse.ui.swt.theme.CSSValueHelper.stubCssColorValue;
import static com.codeaffine.eclipse.ui.swt.theme.TopLevelWindowAttributeContribution.BACKGROUND_COLOR;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.e4.ui.css.swt.helpers.CSSSWTColorHelper.getRGBA;

import org.eclipse.e4.ui.css.swt.dom.ControlElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.css.CSSPrimitiveValue;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.RegistryAdapter;
import com.codeaffine.eclipse.swt.test.util.DisplayHelper;
import com.codeaffine.eclipse.swt.test.util.SWTIgnoreConditions.NonCocoaPlatform;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

@SuppressWarnings("restriction")
public class TopLevelWindowAttributeContributionPDETest {

  private static final String EXTENSION_POINT = "org.eclipse.e4.ui.css.core.propertyHandler";
  private static final CSSPrimitiveValue COLOR = stubCssColorValue( 20, 75, 230 );

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private TopLevelWindowAttributeContribution contribution;
  private Shell shell;

  @Before
  public void setUp() {
    shell = displayHelper.createShell();
    contribution = readContribution();
  }

  @Test
  @ConditionalIgnore( condition = NonCocoaPlatform.class )
  public void propertyHandlerContribution() {
    Extension actual = new RegistryAdapter()
      .readExtension( EXTENSION_POINT )
      .thatMatches( attribute( "handler", TopLevelWindowAttributeContribution.class.getName() ) )
      .process();

    assertThat( actual )
      .hasAttributeValue( "adapter", "org.eclipse.e4.ui.css.swt.dom.WidgetElement" )
      .hasAttributeValue( "composite", "false" )
      .hasChildWithAttributeValue( "name", BACKGROUND_COLOR );
  }

  @Test
  @ConditionalIgnore( condition = NonCocoaPlatform.class )
  public void applyCssProperty() throws Exception {
    Tree control = new Tree( shell, SWT.NONE );

    contribution.applyCSSProperty( newControlElement( control ), BACKGROUND_COLOR, COLOR, null, null );

    assertThat( control.getBackground() ).isEqualTo( expectedColor( COLOR ) );
  }

  @Test
  @ConditionalIgnore( condition = NonCocoaPlatform.class )
  public void getColorTwiceForEqualColorValues() {
    Color color1 = contribution.getColor( COLOR );
    Color color2 = contribution.getColor( COLOR );

    assertThat( color1 ).isSameAs( color2 );
  }

  @Test
  @ConditionalIgnore( condition = NonCocoaPlatform.class )
  public void applyCssPropertyToNonTopLevelWindowElement() throws Exception {
    Tree control = new Tree( new Shell( shell ), SWT.NONE );

    contribution.applyCSSProperty( newControlElement( control ), BACKGROUND_COLOR, COLOR, null, null );

    assertThat( control.getBackground() ).isNotEqualTo( expectedColor( COLOR ) );
  }

  @Test
  @ConditionalIgnore( condition = NonCocoaPlatform.class )
  public void applyCssPropertyWithUnsupportedElementType() {
    Throwable actual = thrownBy( () -> {
      contribution.applyCSSProperty( new Object(), BACKGROUND_COLOR, COLOR, null, null );
    } );

    assertThat( actual ).isNull();
  }

  @Test
  @ConditionalIgnore( condition = NonCocoaPlatform.class )
  public void applyCssPropertyWithUnsupportedProperty() {
    Tree control = new Tree( new Shell( shell ), SWT.NONE );

    Throwable actual = thrownBy( () -> {
      contribution.applyCSSProperty( newControlElement( control ), "unsupported property", COLOR, null, null );
    } );

    assertThat( actual ).isNull();
  }

  private static TopLevelWindowAttributeContribution readContribution() {
    return new RegistryAdapter()
      .createExecutableExtension( EXTENSION_POINT, TopLevelWindowAttributeContribution.class )
      .withTypeAttribute( "handler" )
      .thatMatches( attribute( "handler", TopLevelWindowAttributeContribution.class.getName() ) )
      .process();
  }

  private static ControlElement newControlElement( Control control ) {
    return new ControlElement( control, null );
  }

  private Color expectedColor( CSSPrimitiveValue value ) {
    return new Color( displayHelper.getDisplay(), getRGBA( value ).rgb );
  }
}