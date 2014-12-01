package com.codeaffine.eclipse.ui.progress;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class DefaultPlaceHolderFactoryTest {

  @Test
  public void create() {
    DefaultPlaceHolderFactory factory = new DefaultPlaceHolderFactory();

    PendingUpdatePlaceHolder actual = factory.create();

    assertThat( actual ).isInstanceOf( PendingUpdatePlaceHolder.class );
  }
}