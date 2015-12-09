package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.Demeanor.EXPAND_SCROLL_BAR_ON_MOUSE_OVER;
import static com.codeaffine.eclipse.swt.widget.scrollable.Demeanor.FIXED_SCROLL_BAR_BREADTH;
import static com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar.BAR_BREADTH;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith( Parameterized.class )
public class DemeanorTest {

  @Parameter( 0 )
  public Demeanor demeanor;

  @Parameter( 1 )
  public int expectedBreadth;

  @Parameters
  public static Collection<Object[]> data() {
    Collection<Object[]> result = new ArrayList<>();
    result.add( new Object[] { EXPAND_SCROLL_BAR_ON_MOUSE_OVER, BAR_BREADTH } );
    result.add( new Object[] { FIXED_SCROLL_BAR_BREADTH, BAR_BREADTH * 2 } );
    return result;
  }

  @Test
  public void getBreadth() {
    int actualBreadth = demeanor.getBarBreadth();

    assertThat( actualBreadth ).isEqualTo( expectedBreadth );
  }
}
