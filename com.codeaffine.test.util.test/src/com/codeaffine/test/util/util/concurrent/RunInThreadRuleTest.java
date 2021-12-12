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
package com.codeaffine.test.util.util.concurrent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RunInThreadRuleTest {

  private RunInThreadRule rule;
  private Statement base;

  public static class TestCase {

    @Test
    public void withoutRunInThread() {
    }

    @Test
    @RunInThread
    public void withRunInThread() {
    }
  }

  @Before
  public void setUp() {
    rule = new RunInThreadRule();
    base = mock( Statement.class );
  }

  @Test
  public void applyWithRunInThread() throws Exception {
    Description description = createTestDescription( TestCase.class, "withRunInThread" );

    Statement actual = rule.apply( base, description );

    assertThat( actual ).isInstanceOf( RunInThreadStatement.class );
  }

  @Test
  public void applyWithoutRunInThread() throws Exception {
    Description description = createTestDescription( TestCase.class, "withoutRunInThread" );

    Statement actual = rule.apply( base, description );

    assertThat( actual ).isSameAs( base );
  }

  private static Description createTestDescription( Class<?> testCase, String testName ) throws NoSuchMethodException {
    Annotation[] annotations = TestCase.class.getMethod( testName ).getAnnotations();
    return Description.createTestDescription( testCase, testName, annotations );
  }
}