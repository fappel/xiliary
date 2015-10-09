package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.Predicates.alwaysFalse;
import static com.codeaffine.eclipse.core.runtime.Predicates.alwaysTrue;
import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static com.codeaffine.eclipse.core.runtime.TestExtension.EXTENSION_POINT;
import static com.codeaffine.eclipse.core.runtime.internal.ContributionFinder.ERROR_TOO_MANY_CONTRIBUTIONS;
import static com.codeaffine.eclipse.core.runtime.internal.ContributionFinder.ERROR_ZERO_CONTRIBUTIONS;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.core.runtime.Platform;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.FindException;

public class ReadSingleOperatorPDETest {

  private ReadSingleOperator operator;

  @Before
  public void setUp() {
    operator = new ReadSingleOperator( Platform.getExtensionRegistry(), EXTENSION_POINT );
  }

  @Test
  public void create() {
    operator.setPredicate( attribute( "id", "1" ) );

    Extension actual = operator.create();

    assertThat( actual ).isNotNull();
  }

  @Test
  public void createWithDefaultPredicate() {
    Throwable actual = thrownBy( () -> operator.create() );

    assertThat( actual )
      .isInstanceOf( FindException.class )
      .hasMessage( ERROR_TOO_MANY_CONTRIBUTIONS );
  }

  @Test
  public void setPredicateWithTooManyContributions() {
    Throwable actual = thrownBy( () ->  operator.setPredicate( alwaysTrue() ) );

    assertThat( actual )
      .isInstanceOf( FindException.class )
      .hasMessage( ERROR_TOO_MANY_CONTRIBUTIONS );
  }

  @Test
  public void setPredicateWithZeroContributions() {
    Throwable actual = thrownBy( () -> operator.setPredicate( alwaysFalse() ) );

    assertThat( actual )
      .isInstanceOf( FindException.class )
      .hasMessage( ERROR_ZERO_CONTRIBUTIONS );
  }
}