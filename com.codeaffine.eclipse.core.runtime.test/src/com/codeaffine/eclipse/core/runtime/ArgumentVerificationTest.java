/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.NOT_NULL_PATTERN;
import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.NO_NULL_ELEMENT_PATTERN;
import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNoNullElement;
import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import org.junit.Test;

public class ArgumentVerificationTest {

  private static final String ARGUMENT_NAME = "argumentName";

  @Test
  public void verifyNotNullWithNullArgument() {
    Throwable expected = thrownBy( () ->  verifyNotNull( null, ARGUMENT_NAME ) );

    assertThat( expected )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessage( format( NOT_NULL_PATTERN, ARGUMENT_NAME ) );
  }

  @Test
  public void verifyNotNullWithArgument() {
    Throwable expected = thrownBy( () -> verifyNotNull( new Object(), ARGUMENT_NAME ) );

    assertThat( expected ).isNull();
  }

  @Test
  public void verifyNoNullElementWithNullElement() {
    Collection<Predicate<Extension>> iterable = new ArrayList<>();
    iterable.add( null );

    Throwable expected = thrownBy( () -> verifyNoNullElement( iterable, ARGUMENT_NAME ) );

    assertThat( expected )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessage( format( NO_NULL_ELEMENT_PATTERN, ARGUMENT_NAME ) );
  }

  @Test
  public void verifyNoNullElementWithoutElement() {
    Collection<Predicate<Extension>> iterable = new ArrayList<>();
    iterable.add( Predicates.alwaysFalse() );

    Throwable actual = thrownBy( () -> verifyNoNullElement( iterable, ARGUMENT_NAME ) );

    assertThat( actual ).isNull();
  }
}