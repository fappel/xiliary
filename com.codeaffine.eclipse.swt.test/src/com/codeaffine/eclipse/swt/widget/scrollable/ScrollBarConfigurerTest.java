package com.codeaffine.eclipse.swt.widget.scrollable;

import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.stubContext;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Horizontal.H_VISIBLE;
import static com.codeaffine.eclipse.swt.widget.scrollable.TreeLayoutContextHelper.Vertical.V_INVISIBLE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.junit.Test;

import com.codeaffine.eclipse.swt.widget.scrollbar.FlatScrollBar;

public class ScrollBarConfigurerTest {

  private static final Rectangle VISIBLE_AREA = new Rectangle( 1, 2, 3, 4 );
  private static final Point PREFERRED_SIZE = new Point( 5, 6 );

  @Test
  public void configure() {
    FlatScrollBar scrollBar = mock( FlatScrollBar.class );
    ScrollBarConfigurer configurer = new ScrollBarConfigurer( scrollBar );

    configurer.configure( stubContext( V_INVISIBLE, H_VISIBLE, PREFERRED_SIZE, VISIBLE_AREA ) );

    verify( scrollBar ).setIncrement( 1 );
    verify( scrollBar ).setMaximum( PREFERRED_SIZE.x );
    verify( scrollBar ).setMinimum( 0 );
    verify( scrollBar ).setPageIncrement( VISIBLE_AREA.width );
    verify( scrollBar ).setThumb( VISIBLE_AREA.width );
  }
}