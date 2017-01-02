/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.util;

import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static com.codeaffine.util.ArgumentVerification.NOT_NULL_PATTERN;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ArgumentVerificationTest {

  private static final String MESSAGE = "message";
  private static final String PATTERN = MESSAGE + " <%s>";
  private static final String ARGUMENT_NAME = "argument";

  @Test
  public void verifyConditionOnSuccess() {
    Throwable actual = thrownBy( () -> ArgumentVerification.verifyCondition( true, MESSAGE ) );

    assertThat( actual ).isNull();
  }

  @Test
  public void verifyConditionOnFailure() {
    Throwable actual = thrownBy( () -> ArgumentVerification.verifyCondition( false, MESSAGE ) );

    assertThat( actual )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessage( MESSAGE );
  }

  @Test
  public void verifyConditionOnFailureWithPattern() {
    Throwable actual = thrownBy( () -> ArgumentVerification.verifyCondition( false, PATTERN, ARGUMENT_NAME ) );

    assertThat( actual )
    .isInstanceOf( IllegalArgumentException.class )
    .hasMessage( MESSAGE + " <" + ARGUMENT_NAME + ">" );
  }

  @Test
  public void verifyNotNullOnSuccess() {
    Object expected = new Object();

    Object actual = ArgumentVerification.verifyNotNull( expected, ARGUMENT_NAME );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void verifyNotNullOnFailure() {
    Throwable actual = thrownBy( () -> ArgumentVerification.verifyNotNull( null, ARGUMENT_NAME ) );

    assertThat( actual )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessage( format( NOT_NULL_PATTERN, ARGUMENT_NAME ) );
  }

  @Test
  public void verifyNotNullWithArrayOnSuccess() {
    Object[] expected = new Object[] { new Object() };

    Object[] actual = ArgumentVerification.verifyNotNull( expected, ARGUMENT_NAME );

    assertThat( actual ).isSameAs( expected );
  }

  @Test
  public void verifyNotNullWithArrayOnFailure() {
    Throwable actual = thrownBy( () -> ArgumentVerification.verifyNotNull( ( Object[] )null, ARGUMENT_NAME ) );

    assertThat( actual )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessage( String.format( NOT_NULL_PATTERN, ARGUMENT_NAME ) );
  }

  @Test
  public void verifyNotNullWithMissingArrayElementOnFailure() {
    Throwable actual = thrownBy( () -> ArgumentVerification.verifyNotNull( new Object[] { null }, ARGUMENT_NAME ) );

    assertThat( actual )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessage( String.format( NOT_NULL_PATTERN, ARGUMENT_NAME + "[0]" ) );
  }
}