package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class FindExceptionTest {

  private static final String MESSAGE = "message";

  @Test
  public void constructWithMessage() {
   FindException actual = new FindException( MESSAGE );

   assertThat( actual ).hasMessage( MESSAGE );
  }

  @Test
  public void constructWithNullAsMessage() {
    FindException actual = new FindException( null );

    assertThat( actual ).hasMessage( null );
  }
}