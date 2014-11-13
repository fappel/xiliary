package com.codeaffine.eclipse.swt.widget.scrollbar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class ScrollEventTest {

  private static final int SELECTION = 4711;

  private FlatScrollBar scrollBar;
  private ScrollEvent scrollEvent;

  @Test
  public void getScrollBar() {
    assertThat( scrollEvent.getScrollBar() ).isSameAs( scrollBar );
  }

  @Test
  public void getSelection() {
    assertThat( scrollEvent.getSelection() ).isEqualTo( SELECTION );
  }

  @Before
  public void setUp() {
    scrollBar = mock( FlatScrollBar.class );
    scrollEvent = new ScrollEvent( scrollBar, SELECTION );
  }
}