package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.swt.widgets.ScrollBar;
import org.junit.Before;
import org.junit.Test;


public class VisibilityTest {

  private Visibility visibility;
  private ScrollBar scrollBar;

  @Before
  public void setUp() {
    scrollBar = mock( ScrollBar.class );
    visibility = new Visibility( scrollBar );
  }

  @Test
  public void isVisible() {
    boolean actual = visibility.isVisible();

    assertThat( actual ).isFalse();
  }

  @Test
  public void isVisibleOnScrollBarStateChangeWithoutUpdate() {
    when( scrollBar.isVisible() ).thenReturn( true );

    boolean actual = visibility.isVisible();

    assertThat( actual ).isFalse();
  }

  @Test
  public void isVisibleOnScrollBarStateChangeAndUpdate() {
    when( scrollBar.isVisible() ).thenReturn( true );
    visibility.update();

    boolean actual = visibility.isVisible();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChanged() {
    boolean actual = visibility.hasChanged();

    assertThat( actual ).isFalse();
  }

  @Test
  public void hasChangedOnScrollBarStateChange() {
    when( scrollBar.isVisible() ).thenReturn( true );

    boolean actual = visibility.hasChanged();

    assertThat( actual ).isTrue();
  }

  @Test
  public void hasChangedOnScrollBarStateChangeAndUpdate() {
    when( scrollBar.isVisible() ).thenReturn( true );
    visibility.update();

    boolean actual = visibility.hasChanged();

    assertThat( actual ).isFalse();
  }
}