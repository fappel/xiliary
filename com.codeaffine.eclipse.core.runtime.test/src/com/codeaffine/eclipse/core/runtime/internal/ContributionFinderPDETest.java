package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.DefaultContributionPredicate.ALL;
import static com.codeaffine.eclipse.core.runtime.DefaultContributionPredicate.NONE;
import static com.codeaffine.eclipse.core.runtime.TestExtension.EXTENSION_POINT;
import static com.codeaffine.eclipse.core.runtime.ThrowableCaptor.thrown;
import static com.codeaffine.eclipse.core.runtime.internal.ContributionFinder.ERROR_TOO_MANY_CONTRIBUTIONS;
import static com.codeaffine.eclipse.core.runtime.internal.ContributionFinder.ERROR_ZERO_CONTRIBUTIONS;
import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.ContributionPredicate;
import com.codeaffine.eclipse.core.runtime.FindException;
import com.codeaffine.eclipse.core.runtime.FirstTestContributionPredicate;
import com.codeaffine.eclipse.core.runtime.ThrowableCaptor.Actor;

public class ContributionFinderPDETest {

  private ContributionFinder finder;

  @Before
  public void setUp() {
    finder = new ContributionFinder( Platform.getExtensionRegistry() );
  }

  @Test
  public void find() {
    ContributionPredicate predicate = new FirstTestContributionPredicate();

    IConfigurationElement actual = finder.find( EXTENSION_POINT, predicate );

    assertThat( actual.getAttribute( "id" ) ).isEqualTo( "1" );
  }

  @Test
  public void thatMatchesWithTooManyMatches() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        finder.find( EXTENSION_POINT, ALL );
      }
    } );

    assertThat( actual )
      .isInstanceOf( FindException.class )
      .hasMessage( ERROR_TOO_MANY_CONTRIBUTIONS );
  }

  @Test
  public void thatMatchesWithNoMatches() {
    Throwable actual = thrown( new Actor() {
      @Override
      public void act() throws Throwable {
        finder.find( EXTENSION_POINT, NONE );
      }
    } );

    assertThat( actual )
      .isInstanceOf( FindException.class )
      .hasMessage( ERROR_ZERO_CONTRIBUTIONS );
  }
}