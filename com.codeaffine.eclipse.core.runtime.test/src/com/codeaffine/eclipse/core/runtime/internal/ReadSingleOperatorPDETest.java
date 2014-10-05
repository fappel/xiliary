package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.DefaultContributionPredicate.ALL;
import static com.codeaffine.eclipse.core.runtime.DefaultContributionPredicate.NONE;
import static com.codeaffine.eclipse.core.runtime.TestExtension.EXTENSION_POINT;
import static com.codeaffine.eclipse.core.runtime.ThrowableCaptor.thrown;
import static com.codeaffine.eclipse.core.runtime.internal.ContributionFinder.ERROR_TOO_MANY_CONTRIBUTIONS;
import static com.codeaffine.eclipse.core.runtime.internal.ContributionFinder.ERROR_ZERO_CONTRIBUTIONS;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.core.runtime.Platform;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.FindException;
import com.codeaffine.eclipse.core.runtime.FirstTestContributionPredicate;
import com.codeaffine.eclipse.core.runtime.ThrowableCaptor.Actor;

public class ReadSingleOperatorPDETest {

  private ReadSingleOperator operator;

  @Before
  public void setUp() {
    operator = new ReadSingleOperator( Platform.getExtensionRegistry() );
  }

  @Test
  public void create() {
    operator.setExtensionPointId( EXTENSION_POINT );
    operator.setPredicate( new FirstTestContributionPredicate() );

    Extension actual = operator.create();

    assertThat( actual ).isNotNull();
  }

  @Test
  public void createWithDefaultPredicate() {
    operator.setExtensionPointId( EXTENSION_POINT );

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        operator.create();
      }
    } );

    assertThat( actual )
      .isInstanceOf( FindException.class )
      .hasMessage( ERROR_TOO_MANY_CONTRIBUTIONS );
  }

  @Test
  public void setPredicateWithTooManyContributions() {
    operator.setExtensionPointId( EXTENSION_POINT );

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        operator.setPredicate( ALL );
      }
    } );

    assertThat( actual )
    .isInstanceOf( FindException.class )
    .hasMessage( ERROR_TOO_MANY_CONTRIBUTIONS );
  }

  @Test
  public void setPredicateWithZeroContributions() {
    operator.setExtensionPointId( EXTENSION_POINT );

    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        operator.setPredicate( NONE );
      }
    } );

    assertThat( actual )
      .isInstanceOf( FindException.class )
      .hasMessage( ERROR_ZERO_CONTRIBUTIONS );
  }
}