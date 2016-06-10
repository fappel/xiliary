package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.swt.widget.scrollable.context.ScrollableControl;

public class ScrollableRedrawStateTest {

  private ScrollableRedrawState redrawState;
  private ScrollableControl<?> scrollable;

  @Before
  public void setUp() {
    scrollable = mock( ScrollableControl.class );
    redrawState = new ScrollableRedrawState( scrollable );
  }

  @Test
  public void updateWithRedrawSuspension() {
    redrawState.update( false );

    verify( scrollable ).setRedraw( false );
  }

  @Test
  public void updateWithRedrawSuspensionIfAlreadySuspended() {
    redrawState.update( false );
    reset( new ScrollableControl[] { scrollable } );

    redrawState.update( false );

    verify( scrollable, never() ).setRedraw( anyBoolean() );
  }

  @Test
  public void updateToResolveRedrawSuspension() {
    redrawState.update( false );
    reset( new ScrollableControl[] { scrollable } );

    redrawState.update( true );

    verify( scrollable ).setRedraw( true );
  }

  @Test
  public void updateToResolveRedrawSuspensionIfAlreadyResolved() {
    redrawState.update( false );
    redrawState.update( true );
    reset( new ScrollableControl[] { scrollable } );

    redrawState.update( true );

    verify( scrollable, never() ).setRedraw( anyBoolean() );
  }
}