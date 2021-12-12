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
package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.Predicates.alwaysFalse;
import static com.codeaffine.eclipse.core.runtime.Predicates.alwaysTrue;
import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static com.codeaffine.eclipse.core.runtime.TestExtension.EXTENSION_POINT;
import static com.codeaffine.eclipse.core.runtime.internal.ContributionFinder.ERROR_TOO_MANY_CONTRIBUTIONS;
import static com.codeaffine.eclipse.core.runtime.internal.ContributionFinder.ERROR_ZERO_CONTRIBUTIONS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Predicate;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;
import com.codeaffine.eclipse.core.runtime.FindException;
import com.codeaffine.test.util.lang.ThrowableCaptor;

public class ContributionFinderPDETest {

  private ContributionFinder finder;

  @Before
  public void setUp() {
    finder = new ContributionFinder( Platform.getExtensionRegistry() );
  }

  @Test
  public void find() {
    Predicate<Extension> predicate = attribute( "id", "1" );

    IConfigurationElement actual = finder.find( EXTENSION_POINT, predicate );

    assertThat( actual.getAttribute( "id" ) ).isEqualTo( "1" );
  }

  @Test
  public void thatMatchesWithTooManyMatches() {
    Throwable actual = ThrowableCaptor.thrownBy( () -> finder.find( EXTENSION_POINT, alwaysTrue() ) );

    assertThat( actual )
      .isInstanceOf( FindException.class )
      .hasMessage( ERROR_TOO_MANY_CONTRIBUTIONS );
  }

  @Test
  public void thatMatchesWithNoMatches() {
    Throwable actual = ThrowableCaptor.thrownBy( () -> finder.find( EXTENSION_POINT, alwaysFalse() ) );

    assertThat( actual )
      .isInstanceOf( FindException.class )
      .hasMessage( ERROR_ZERO_CONTRIBUTIONS );
  }
}