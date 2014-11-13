package com.codeaffine.eclipse.swt.widget.scrollbar;

import static com.codeaffine.eclipse.swt.widget.scrollbar.OrientationHelper.stubSizeComputation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.junit.Test;

public class FlatScrollBarLayoutTest {

  private static final int W_HINT = 10;
  private static final int H_HINT = 20;

  @Test
  public void computeSize() {
    Point expected = new Point( 100, 200 );
    Composite composite = mock( Composite.class );
    Orientation orientation = stubSizeComputation( composite, expected, W_HINT, H_HINT, true );
    FlatScrollBarLayout layout = new FlatScrollBarLayout( orientation );

    Point actual = layout.computeSize( composite, W_HINT, H_HINT, true );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void layoutDoesNothing() {
    FlatScrollBarLayout layout = new FlatScrollBarLayout( null );
    Composite composite = mock( Composite.class );

    layout.layout( composite, true );

    verifyNoMoreInteractions( composite );
  }
}