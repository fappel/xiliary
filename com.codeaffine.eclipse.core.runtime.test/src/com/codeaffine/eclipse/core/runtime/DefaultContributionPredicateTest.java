package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class DefaultContributionPredicateTest {

  @Test
  public void applyAll() {
    ContributionPredicate applyAll = DefaultContributionPredicate.ALL;

    boolean actual = applyAll.apply( null );

    assertThat( actual ).isTrue();
  }

  @Test
  public void applyNone() {
    ContributionPredicate applyAll = DefaultContributionPredicate.NONE;

    boolean actual = applyAll.apply( null );

    assertThat( actual ).isFalse();
  }
}