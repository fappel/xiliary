package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ExtensionExceptionTest {

  private static final String MESSAGE = "message";

  @Test
  public void constructorWithCause() {
    Throwable cause = new Throwable( MESSAGE );

    ExtensionException actual = new ExtensionException( cause );

    assertThat( actual )
      .hasCauseExactlyInstanceOf( Throwable.class )
      .hasMessage( cause.getClass().getName() + ": " + MESSAGE );
  }

  @Test
  public void constructorWithNullAsCause() {
    ExtensionException actual = new ExtensionException( null );

    assertThat( actual )
    .hasMessage( null )
    .hasNoCause();
  }
}