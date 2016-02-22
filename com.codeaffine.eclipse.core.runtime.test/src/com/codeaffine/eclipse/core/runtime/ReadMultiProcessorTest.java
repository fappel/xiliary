/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.core.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.function.Predicate;

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
    Predicate<Extension> expected = mock( Predicate.class );

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