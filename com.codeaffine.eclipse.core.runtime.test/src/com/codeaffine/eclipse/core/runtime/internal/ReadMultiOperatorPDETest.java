/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.eclipse.core.runtime.internal;

import static com.codeaffine.eclipse.core.runtime.Predicates.attribute;
import static com.codeaffine.eclipse.core.runtime.TestExtension.EXTENSION_POINT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.core.runtime.Platform.getExtensionRegistry;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.codeaffine.eclipse.core.runtime.Extension;

public class ReadMultiOperatorPDETest {

  private ReadMultiOperator operator;

  @Before
  public void setUp() {
    operator = new ReadMultiOperator( getExtensionRegistry(), EXTENSION_POINT );
  }

  @Test
  public void create() {
    Collection<Extension> actuals = operator.create();

    assertThat( actuals ).hasSize( 2 );
  }

  @Test
  public void createWithPredication() {
    operator.setPredicate( attribute( "id", "1" ) );

    Collection<Extension> actuals = operator.create();

    assertThat( actuals ).hasSize( 1 );
  }
}