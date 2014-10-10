package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionsOperator;

@SuppressWarnings("unchecked")
public class ReadMultiProcessorTest {

  private ReadExtensionsOperator<Runnable> operator;
  private ReadMultiProcessor<Runnable> processor;

  @Before
  public void setUp() {
    operator = mock( ReadExtensionsOperator.class );
    processor = new ReadMultiProcessor<Runnable>( operator );
  }

  @Test
  public void thatMatches() {
    Predicate expected = mock( Predicate.class );

    ReadMultiProcessor<Runnable> actual = processor.thatMatches( expected );

    assertThat( actual ).isSameAs( processor );
    verify( operator ).setPredicate( expected );
  }

  @Test( expected = IllegalArgumentException.class )
  public void thatMatchesWithNullAsContributionPredicate() {
    processor.thatMatches( null );
  }

  @Test
  public void process() {
    Collection<Runnable> actuals = processor.process();

    assertThat( actuals ).isNotNull();
    verify( operator ).create();
  }
}