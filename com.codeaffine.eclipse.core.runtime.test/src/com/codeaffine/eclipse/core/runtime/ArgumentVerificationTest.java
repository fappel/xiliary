package com.codeaffine.eclipse.core.runtime;

import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.NOT_NULL_PATTERN;
import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.NO_NULL_ELEMENT_PATTERN;
import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNoNullElement;
import static com.codeaffine.eclipse.core.runtime.ArgumentVerification.verifyNotNull;
import static com.codeaffine.eclipse.core.runtime.ThrowableCaptor.thrown;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.ThrowableCaptor.Actor;

public class ArgumentVerificationTest {

  private static final String ARGUMENT_NAME = "argumentName";

  @Test
  public void verifyNotNullWithNullArgument() {
    Throwable expected = thrown( new Actor() {
      @Override public void act() {
        verifyNotNull( null, ARGUMENT_NAME );
      }
    } );

    assertThat( expected )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessage( format( NOT_NULL_PATTERN, ARGUMENT_NAME ) );
  }

  @Test
  public void verifyNotNullWithArgument() {
    Throwable expected = thrown( new Actor() {
      @Override public void act() {
        verifyNotNull( new Object(), ARGUMENT_NAME );
      }
    } );

    assertThat( expected ).isNull();
  }

  @Test
  public void verifyNoNullElementWithNullElement() {
    final Collection<Predicate> iterable = new ArrayList<Predicate>();
    iterable.add( null );

    Throwable expected = thrown( new Actor() {
      @Override public void act() {
        verifyNoNullElement( iterable, ARGUMENT_NAME );
      }
    } );

    assertThat( expected )
      .isInstanceOf( IllegalArgumentException.class )
      .hasMessage( format( NO_NULL_ELEMENT_PATTERN, ARGUMENT_NAME ) );
  }

  @Test
  public void verifyNoNullElementWithoutElement() {
    final Collection<Predicate> iterable = new ArrayList<Predicate>();
    iterable.add( Predicates.alwaysFalse() );

    Throwable expected = thrown( new Actor() {
      @Override public void act() {
        verifyNoNullElement( iterable, ARGUMENT_NAME );
      }
    } );

    assertThat( expected ).isNull();
  }
}