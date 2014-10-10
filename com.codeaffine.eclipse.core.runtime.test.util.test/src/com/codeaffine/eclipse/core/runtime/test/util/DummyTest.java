package com.codeaffine.eclipse.core.runtime.test.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class DummyTest {

  @Test
  public void doit() {
    assertThat( new Dummy().doit() ).isTrue();
  }
}
