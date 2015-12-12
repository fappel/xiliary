package com.codeaffine.eclipse.swt.layout;

import static com.codeaffine.eclipse.swt.layout.FillLayoutAssert.assertThat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.eclipse.swt.test.util.DisplayHelper;

public class FillLayoutsTest {

  @Rule public final DisplayHelper displayHelper = new DisplayHelper();

  private Composite composite;

  @Before
  public void setUp() {
    composite = displayHelper.createShell();
  }

  @Test
  public void applyFillLayoutTo() {
    FillLayouts.applyFillLayoutTo( composite );

    assertThat( actualLayout() ).isEqualTo( new FillLayout() );
  }

  @Test
  public void withSpacing() {
    int expected = 9;

    FillLayouts.applyFillLayoutTo( composite ).withSpacing( expected );

    assertThat( actualLayout() ).hasSpacing( expected );
  }

  @Test
  public void withType() {
    int expected = SWT.HORIZONTAL;

    FillLayouts.applyFillLayoutTo( composite ).withType( expected );

    assertThat( actualLayout() ).hasType( expected );
  }

  @Test
  public void withMargin() {
    int expectedWidht = 8;
    int expectedHeight = 6;

    FillLayouts.applyFillLayoutTo( composite ).withMargin( expectedWidht, expectedHeight );

    assertThat( actualLayout() )
      .hasMarginWidth( expectedWidht )
      .hasMarginHeight( expectedHeight );
  }

  @Test
  public void withMarginSameForWidthAndHeight() {
    int expected = 8;

    FillLayouts.applyFillLayoutTo( composite ).withMargin( expected );

    assertThat( actualLayout() )
      .hasMarginWidth( expected )
      .hasMarginHeight( expected );
  }

  @Test
  public void withMarginWidth() {
    int expected = 8;

    FillLayouts.applyFillLayoutTo( composite ).withMarginWidth( expected );

    assertThat( actualLayout() )
      .hasMarginWidth( expected )
      .hasMarginHeight( 0 );
  }

  @Test
  public void withMarginHeight() {
    int expected = 8;

    FillLayouts.applyFillLayoutTo( composite ).withMarginHeight( expected );

    assertThat( actualLayout() )
      .hasMarginWidth( 0 )
      .hasMarginHeight( expected );
  }

  private FillLayout actualLayout() {
    return ( FillLayout )composite.getLayout();
  }
}