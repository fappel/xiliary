package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.internal.Operator.ReadExtensionOperator;

@SuppressWarnings("unchecked")
public class SingleProcessorTest {

  private ReadExtensionOperator<Object> operator;
  private SingleProcessor<Object> creator;

  @Before
  public void setUp() {
    operator = mock( ReadExtensionOperator.class );
    creator = new SingleProcessor<Object>( operator );
  }

  @Test
  public void thatMatches() {
    Predicate expected = mock( Predicate.class );

    SingleProcessor<Object> actual = creator.thatMatches( expected );

    assertThat( actual ).isSameAs( creator );
    verify( operator ).setPredicate( expected );
  }

  @Test( expected = IllegalArgumentException.class )
  public void thatMatchesWithNullAsContributionPredicate() {
    creator.thatMatches( null );
  }

  @Test
  public void process() {
    Object expected = equipOperatorWithSingleExtension();

    Object actual = creator.process();

    assertThat( actual ).isSameAs( expected );
  }

  private Object equipOperatorWithSingleExtension() {
    Object result = new Object();
    when( operator.create() ).thenReturn( result );
    return result;
  }
}