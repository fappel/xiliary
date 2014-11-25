package com.codeaffine.eclipse.swt.widget.scrollbar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class ResizeObserverTest {

  @Test
  public void controlResized() {
    FlatScrollBar scrollBar = mock( FlatScrollBar.class );
    ResizeObserver observer = new ResizeObserver( scrollBar );

    observer.controlResized( null );

    verify( scrollBar ).layout();
    verify( scrollBar ).moveAbove( null );
  }
}