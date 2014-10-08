package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionsOperator;

@SuppressWarnings("unchecked")
public class MultiProcessorTest {

  private MultiProcessor<Runnable> collector;
  private ReadExtensionsOperator<Runnable> operator;

  @Before
  public void setUp() {
    operator = mock( ReadExtensionsOperator.class );
    collector = new MultiProcessor<Runnable>( operator );
  }

  @Test
  public void thatMatches() {
    Predicate expected = mock( Predicate.class );

    MultiProcessor<Runnable> actual = collector.thatMatches( expected );

    assertThat( actual ).isSameAs( collector );
    verify( operator ).setPredicate( expected );
  }

  @Test( expected = IllegalArgumentException.class )
  public void thatMatchesWithNullAsContributionPredicate() {
    collector.thatMatches( null );
  }

  @Test
  public void process() {
    Collection<Runnable> actuals = collector.process();

    assertThat( actuals ).isNotNull();
    verify( operator ).create();
  }
}