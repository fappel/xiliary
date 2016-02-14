package com.codeaffine.eclipse.swt.widget.scrollable;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SizeObserverTest {

  @Test
  public void mustLayoutAdapter() {
    SizeObserver sizeObserver = new SizeObserver() {};

    boolean actual = sizeObserver.mustLayoutAdapter();

    assertThat( actual ).isFalse();
  }
}
