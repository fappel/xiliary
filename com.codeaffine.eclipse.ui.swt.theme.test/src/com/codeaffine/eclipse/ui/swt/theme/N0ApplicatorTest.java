package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_INCREMENT_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.CSSValueHelper.stubCssIntValue;
import static com.codeaffine.eclipse.ui.swt.theme.CSSValueHelper.stubCssStringValue;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_INCREMENT_LENGTH;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.TOP_LEVEL_WINDOW_SELECTOR;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static java.lang.Integer.parseInt;
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
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule;
import com.codeaffine.test.util.junit.ConditionalIgnoreRule.ConditionalIgnore;

public class N0ApplicatorTest {

  private static final CSSPrimitiveValue CSS_INCREMENT = stubCssIntValue( 7 );

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ApplicatorTestHelper applicatorTestHelper;
  private N0Applicator applicator;
  private Scrollable scrollable;
  private Shell shell;

  @Before
  public void setUp() {
    shell = displayHelper.createShell();
    scrollable = new Tree( shell, SWT.NONE );
    applicatorTestHelper = new ApplicatorTestHelper( scrollable );
    applicator = new N0Applicator( applicatorTestHelper.getFactory() );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void apply() {
    ScrollbarStyle style = applicatorTestHelper.adapt();

    applicator.apply( scrollable, CSS_INCREMENT, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );

    assertThat( style.getIncrementButtonLength() ).isEqualTo( expectedValue( CSS_INCREMENT ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyTopLevelWindowAttribute() {
    String attribute = FLAT_SCROLL_BAR_INCREMENT_LENGTH + TOP_LEVEL_WINDOW_SELECTOR;
    ScrollbarStyle style = applicatorTestHelper.adapt();

    applicator.apply( scrollable, CSS_INCREMENT, attribute, FLAT_SCROLLBAR_INCREMENT_SETTER );

    assertThat( style.getIncrementButtonLength() ).isEqualTo( expectedValue( CSS_INCREMENT ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyTopLevelWindowAttributeOnChildShell() {
    String attribute = FLAT_SCROLL_BAR_INCREMENT_LENGTH + TOP_LEVEL_WINDOW_SELECTOR;
    applicatorTestHelper.reparentScrollableOnChildShell();
    ScrollbarStyle style = applicatorTestHelper.adapt();

    applicator.apply( scrollable, CSS_INCREMENT, attribute, FLAT_SCROLLBAR_INCREMENT_SETTER );

    assertThat( style.getIncrementButtonLength() ).isNotEqualTo( expectedValue( CSS_INCREMENT ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyWithBuffering() {
    applicator.apply( scrollable, CSS_INCREMENT, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );
    ScrollbarStyle style = applicatorTestHelper.adapt();
    applicator.apply( scrollable, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );

    assertThat( style.getIncrementButtonLength() ).isEqualTo( expectedValue( CSS_INCREMENT ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyTopLevelWindowAttributeWithBuffering() {
    String attribute = FLAT_SCROLL_BAR_INCREMENT_LENGTH + TOP_LEVEL_WINDOW_SELECTOR;
    applicator.apply( scrollable, CSS_INCREMENT, attribute, FLAT_SCROLLBAR_INCREMENT_SETTER );
    ScrollbarStyle style = applicatorTestHelper.adapt();
    applicator.apply( scrollable, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );

    assertThat( style.getIncrementButtonLength() ).isEqualTo( expectedValue( CSS_INCREMENT ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyTopLevelWindowAttributeWithBufferingOnChildShell() {
    String attribute = FLAT_SCROLL_BAR_INCREMENT_LENGTH + TOP_LEVEL_WINDOW_SELECTOR;
    applicatorTestHelper.reparentScrollableOnChildShell();
    applicator.apply( scrollable, CSS_INCREMENT, attribute, FLAT_SCROLLBAR_INCREMENT_SETTER );
    ScrollbarStyle style = applicatorTestHelper.adapt();
    applicator.apply( scrollable, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );

    assertThat( style.getIncrementButtonLength() ).isNotEqualTo( expectedValue( CSS_INCREMENT ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyWithNegativeCSSValue() {
    applicatorTestHelper.adapt();

    Throwable actual = thrownBy( () -> {
      CSSPrimitiveValue negativeInt = stubCssIntValue( -1 );
      applicator.apply( scrollable, negativeInt, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );
    } );

    assertThat( actual )
      .hasMessageContaining( FLAT_SCROLL_BAR_INCREMENT_LENGTH )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyWithNonIntCSSValue() {
    applicatorTestHelper.adapt();

    Throwable actual = thrownBy( () -> {
      CSSPrimitiveValue bad = stubCssStringValue( "bad" );
      applicator.apply( scrollable, bad, FLAT_SCROLL_BAR_INCREMENT_LENGTH, FLAT_SCROLLBAR_INCREMENT_SETTER );
    } );

    assertThat( actual )
      .hasMessageContaining( FLAT_SCROLL_BAR_INCREMENT_LENGTH )
      .hasMessageContaining( "bad" )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  public void parse() {
    Integer actual = N0Applicator.parse( "1", FLAT_SCROLL_BAR_INCREMENT_LENGTH );

    assertThat( actual ).isSameAs( Integer.valueOf( 1 ) );
  }

  @Test
  public void parseWithNegativeValue() {
    Throwable actual = thrownBy( () -> N0Applicator.parse( "-1", FLAT_SCROLL_BAR_INCREMENT_LENGTH ) );

    assertThat( actual )
      .hasMessageContaining( FLAT_SCROLL_BAR_INCREMENT_LENGTH )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  public void parseWithNonIntValue() {
    Throwable actual = thrownBy( () -> N0Applicator.parse( "bad", FLAT_SCROLL_BAR_INCREMENT_LENGTH ) );

    assertThat( actual )
      .hasMessageContaining( FLAT_SCROLL_BAR_INCREMENT_LENGTH )
      .hasMessageContaining( "bad" )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  public void parseWithNullAsValue() {
    Throwable actual = thrownBy( () -> N0Applicator.parse( null, FLAT_SCROLL_BAR_INCREMENT_LENGTH ) );

    assertThat( actual )
      .hasMessageContaining( "value" )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  public void parseWithNullAsAttribute() {
    Throwable actual = thrownBy( () -> N0Applicator.parse( "1", null ) );

    assertThat( actual )
     .hasMessageContaining( "attribute" )
     .isInstanceOf( IllegalArgumentException.class );
  }

  private static int expectedValue( CSSPrimitiveValue cssValue ) {
    return parseInt( cssValue.getCssText() );
  }
}