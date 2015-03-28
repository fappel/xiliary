package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;
import static com.codeaffine.eclipse.swt.widget.scrollable.LayoutContext.OVERLAY_OFFSET;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Scrollable;

class LayoutContextHelper {

  static final int STUB_VERTICAL_BAR_OFFSET = OVERLAY_OFFSET - BAR_BREADTH;
  static final int OFFSET = 0;
  static final int BORDER_WIDTH = 2;
  static final int BORDER_ADJUSTMENT = BORDER_WIDTH * 2;

  enum Horizontal {
    H_VISIBLE( true ), H_INVISIBLE( false );

    final boolean value;

    Horizontal( boolean value ) {
      this.value = value;
    }
  }

  enum Vertical {
    V_VISIBLE( true ), V_INVISIBLE( false );

    final boolean value;

    Vertical( boolean value ) {
      this.value = value;
    }
  }

  static LayoutContext<Scrollable> stubContext(
    Vertical verticalBarVisible, Horizontal horizontalBarVisible, Point preferredSize, Rectangle area )
  {
    @SuppressWarnings("unchecked")
    LayoutContext<Scrollable> result = mock( LayoutContext.class );
    when( result.isVerticalBarVisible() ).thenReturn( verticalBarVisible.value );
    when( result.isHorizontalBarVisible() ).thenReturn( horizontalBarVisible.value );
    when( result.getPreferredSize() ).thenReturn( preferredSize );
    when( result.getVerticalBarOffset() ).thenReturn( STUB_VERTICAL_BAR_OFFSET );
    when( result.getOffset() ).thenReturn( OFFSET );
    when( result.getBorderWidth() ).thenReturn( BORDER_WIDTH );
    when( result.getVisibleArea() )
      .thenReturn( new Rectangle( area.x, area.y, area.width + BORDER_ADJUSTMENT, area.height + BORDER_ADJUSTMENT ) );
    when( result.getOriginOfScrollableOrdinates() )
      .thenReturn( new Point( area.x - BORDER_WIDTH, area.y - BORDER_WIDTH ) );
    return result;
  }
}