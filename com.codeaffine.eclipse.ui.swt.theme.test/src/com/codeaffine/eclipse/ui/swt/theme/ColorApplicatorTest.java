package com.codeaffine.eclipse.ui.swt.theme;

import static com.codeaffine.eclipse.ui.swt.theme.AttributeApplicator.attach;
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
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollableAdapterFactory;
import com.codeaffine.eclipse.swt.widget.scrollable.ScrollbarStyle;
import com.codeaffine.eclipse.swt.widget.scrollable.TreeAdapter;

@SuppressWarnings("restriction")
public class ColorApplicatorTest {

  private static final CSSPrimitiveValue CSS_COLOR = stubCssColorValue( 0, 8, 15 );

  @Rule
  public final DisplayHelper displayHelper = new DisplayHelper();

  private ScrollableAdapterFactory factory;
  private ColorApplicator applicator;
  private Scrollable scrollable;
  private Shell shell;

  @Before
  public void setUp() {
    shell = displayHelper.createShell();
    scrollable = new Tree( shell, SWT.NONE );
    factory = new ScrollableAdapterFactory();
    applicator = new ColorApplicator( factory );
  }

  @Test
  public void apply() {
    ScrollbarStyle style = adapt();

    applicator.apply( scrollable, CSS_COLOR, FLAT_SCROLL_BAR_BACKGROUND, FLAT_SCROLLBAR_BACKGROUND_SETTER );

    assertThat( style.getBackgroundColor().getRGBA() ).isEqualTo( getRGBA( CSS_COLOR ) );
  }

  @Test
  public void applyTopLevelWindowAttribute() {
    String attribute = FLAT_SCROLL_BAR_BACKGROUND + TOP_LEVEL_WINDOW_SELECTOR;
    ScrollbarStyle style = adapt();

    applicator.apply( scrollable, CSS_COLOR, attribute, FLAT_SCROLLBAR_BACKGROUND_SETTER );

    assertThat( style.getBackgroundColor().getRGBA() ).isEqualTo( getRGBA( CSS_COLOR ) );
  }
  @Test
  public void applyTopLevelWindowAttributeOnChildShell() {
    String attribute = FLAT_SCROLL_BAR_BACKGROUND + TOP_LEVEL_WINDOW_SELECTOR;
    reparentScrollableOnChildShell();
    ScrollbarStyle style = adapt();

    applicator.apply( scrollable, CSS_COLOR, attribute, FLAT_SCROLLBAR_BACKGROUND_SETTER );

    assertThat( style.getBackgroundColor().getRGBA() ).isNotEqualTo( getRGBA( CSS_COLOR ) );
  }

  @Test
  public void applyWithBuffering() {
    applicator.apply( scrollable, CSS_COLOR, FLAT_SCROLL_BAR_BACKGROUND, FLAT_SCROLLBAR_BACKGROUND_SETTER );
    ScrollbarStyle style = adapt();
    applicator.apply( scrollable, FLAT_SCROLL_BAR_BACKGROUND, FLAT_SCROLLBAR_BACKGROUND_SETTER );

    assertThat( style.getBackgroundColor().getRGBA() ).isEqualTo( getRGBA( CSS_COLOR ) );
  }

  @Test
  public void applyTopLevelWindowAttributeWithBuffering() {
    String attribute = FLAT_SCROLL_BAR_BACKGROUND + TOP_LEVEL_WINDOW_SELECTOR;
    applicator.apply( scrollable, CSS_COLOR, attribute, FLAT_SCROLLBAR_BACKGROUND_SETTER );
    ScrollbarStyle style = adapt();
    applicator.apply( scrollable, FLAT_SCROLL_BAR_BACKGROUND, FLAT_SCROLLBAR_BACKGROUND_SETTER );

    assertThat( style.getBackgroundColor().getRGBA() ).isEqualTo( getRGBA( CSS_COLOR ) );
  }

  @Test
  public void applyTopLevelWindowAttributeWithBufferingOnChildShell() {
    String attribute = FLAT_SCROLL_BAR_BACKGROUND + TOP_LEVEL_WINDOW_SELECTOR;
    reparentScrollableOnChildShell();
    applicator.apply( scrollable, CSS_COLOR, attribute, FLAT_SCROLLBAR_BACKGROUND_SETTER );
    ScrollbarStyle style = adapt();
    applicator.apply( scrollable, FLAT_SCROLL_BAR_BACKGROUND, FLAT_SCROLLBAR_BACKGROUND_SETTER );

    assertThat( style.getBackgroundColor().getRGBA() ).isNotEqualTo( getRGBA( CSS_COLOR ) );
  }

  private void reparentScrollableOnChildShell() {
    Shell childShell = new Shell( shell );
    scrollable.setParent( childShell );
  }

  private ScrollbarStyle adapt() {
    ScrollbarStyle result = factory.create( ( Tree )scrollable, TreeAdapter.class );
    attach( scrollable, result );
    return result;
  }
}