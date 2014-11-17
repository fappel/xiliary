package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.FlatScrollBarTree.BAR_BREADTH;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContext.OVERLAY_OFFSET;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

class TreeLayoutContextHelper {

  static final int STUB_OFFSET = OVERLAY_OFFSET - BAR_BREADTH;

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

  static TreeLayoutContext stubContext(
    Vertical verticalBarVisible, Horizontal horizontalBarVisible, Point preferredSize, Rectangle visibleArea )
  {
    TreeLayoutContext result = mock( TreeLayoutContext.class );
    when( result.isVerticalBarVisible() ).thenReturn( verticalBarVisible.value );
    when( result.isHorizontalBarVisible() ).thenReturn( horizontalBarVisible.value );
    when( result.getPreferredSize() ).thenReturn( preferredSize );
    when( result.getVisibleArea() ).thenReturn( visibleArea );
    when( result.getVerticalBarOffset() ).thenReturn( STUB_OFFSET );
    return result;
  }
}