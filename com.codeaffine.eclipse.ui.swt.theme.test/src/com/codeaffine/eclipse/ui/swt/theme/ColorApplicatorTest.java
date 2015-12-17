package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.AttributeSetter.FLAT_SCROLLBAR_BACKGROUND_SETTER;
import static com.codeaffine.eclipse.ui.swt.theme.CSSValueHelper.stubCssColorValue;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.FLAT_SCROLL_BAR_BACKGROUND;
import static com.codeaffine.eclipse.ui.swt.theme.ScrollableAdapterContribution.TOP_LEVEL_WINDOW_SELECTOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.e4.ui.css.swt.helpers.CSSSWTColorHelper.getRGBA;

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

@SuppressWarnings("restriction")
public class ColorApplicatorTest {

  private static final CSSPrimitiveValue CSS_COLOR = stubCssColorValue( 0, 8, 15 );

  @Rule public final ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();
  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private ApplicatorTestHelper applicatorTestHelper;
  private ColorApplicator applicator;
  private Scrollable scrollable;
  private Shell shell;


  @Before
  public void setUp() {
    shell = displayHelper.createShell();
    scrollable = new Tree( shell, SWT.NONE );
    applicatorTestHelper = new ApplicatorTestHelper( scrollable );
    applicator = new ColorApplicator( applicatorTestHelper.getFactory() );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void apply() {
    ScrollbarStyle style = applicatorTestHelper.adapt();

    applicator.apply( scrollable, CSS_COLOR, FLAT_SCROLL_BAR_BACKGROUND, FLAT_SCROLLBAR_BACKGROUND_SETTER );

    assertThat( style.getBackgroundColor().getRGBA() ).isEqualTo( getRGBA( CSS_COLOR ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyTopLevelWindowAttribute() {
    String attribute = FLAT_SCROLL_BAR_BACKGROUND + TOP_LEVEL_WINDOW_SELECTOR;
    ScrollbarStyle style = applicatorTestHelper.adapt();

    applicator.apply( scrollable, CSS_COLOR, attribute, FLAT_SCROLLBAR_BACKGROUND_SETTER );

    assertThat( style.getBackgroundColor().getRGBA() ).isEqualTo( getRGBA( CSS_COLOR ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyTopLevelWindowAttributeOnChildShell() {
    String attribute = FLAT_SCROLL_BAR_BACKGROUND + TOP_LEVEL_WINDOW_SELECTOR;
    applicatorTestHelper.reparentScrollableOnChildShell();
    ScrollbarStyle style = applicatorTestHelper.adapt();

    applicator.apply( scrollable, CSS_COLOR, attribute, FLAT_SCROLLBAR_BACKGROUND_SETTER );

    assertThat( style.getBackgroundColor().getRGBA() ).isNotEqualTo( getRGBA( CSS_COLOR ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyWithBuffering() {
    applicator.apply( scrollable, CSS_COLOR, FLAT_SCROLL_BAR_BACKGROUND, FLAT_SCROLLBAR_BACKGROUND_SETTER );
    ScrollbarStyle style = applicatorTestHelper.adapt();
    applicator.apply( scrollable, FLAT_SCROLL_BAR_BACKGROUND, FLAT_SCROLLBAR_BACKGROUND_SETTER );

    assertThat( style.getBackgroundColor().getRGBA() ).isEqualTo( getRGBA( CSS_COLOR ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyTopLevelWindowAttributeWithBuffering() {
    String attribute = FLAT_SCROLL_BAR_BACKGROUND + TOP_LEVEL_WINDOW_SELECTOR;
    applicator.apply( scrollable, CSS_COLOR, attribute, FLAT_SCROLLBAR_BACKGROUND_SETTER );
    ScrollbarStyle style = applicatorTestHelper.adapt();
    applicator.apply( scrollable, FLAT_SCROLL_BAR_BACKGROUND, FLAT_SCROLLBAR_BACKGROUND_SETTER );

    assertThat( style.getBackgroundColor().getRGBA() ).isEqualTo( getRGBA( CSS_COLOR ) );
  }

  @Test
  @ConditionalIgnore( condition = GtkPlatform.class )
  public void applyTopLevelWindowAttributeWithBufferingOnChildShell() {
    String attribute = FLAT_SCROLL_BAR_BACKGROUND + TOP_LEVEL_WINDOW_SELECTOR;
    applicatorTestHelper.reparentScrollableOnChildShell();
    applicator.apply( scrollable, CSS_COLOR, attribute, FLAT_SCROLLBAR_BACKGROUND_SETTER );
    ScrollbarStyle style = applicatorTestHelper.adapt();
    applicator.apply( scrollable, FLAT_SCROLL_BAR_BACKGROUND, FLAT_SCROLLBAR_BACKGROUND_SETTER );

    assertThat( style.getBackgroundColor().getRGBA() ).isNotEqualTo( getRGBA( CSS_COLOR ) );
  }
}