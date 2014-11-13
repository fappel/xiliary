package com.codeaffine.eclipse.swt.widget.scrollbar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public class OrientationHelper {

  static Orientation stubSizeComputation(
    Composite composite, Point returnValue, int wHint, int hHint, boolean flushCache )
  {
    Orientation result = mock( Orientation.class );
    when( result.computeSize( composite, wHint, hHint, flushCache ) ).thenReturn( returnValue );
    return result;
  }

  public static Collection<Object[]> valuesForParameterizedTests() {
    Collection<Object[]> result = new ArrayList<Object[]>();
    for( Orientation orientation : Orientation.values() ) {
      result.add( new Object[] { orientation } );
    }
    return result;
  }
}