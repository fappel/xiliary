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
package com.codeaffine.osgi.test.util;

import static com.codeaffine.osgi.test.util.ServiceCollector.collectServices;
import static com.codeaffine.test.util.lang.ThrowableCaptor.thrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Hashtable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.Statement;

public class ServiceRegistrationRulePDETest {

  private ServiceRegistrationRule rule;

  @Before
  public void setUp() {
    rule = new ServiceRegistrationRule( getClass() );
  }

  @After
  public void tearDown() {
    rule.cleanup();
  }

  @Test
  public void register() {
    Registration<TestService> registration = rule.register( TestService.class, new TestServiceImpl() );

    assertThat( registration ).isNotNull();
    assertThat( registration.getServiceRegistration() ).isNotNull();
    assertThat( collectServices( TestService.class, TestServiceImpl.class ) ).hasSize( 1 );
  }

  @Test
  public void registerWithProperties() {
    Hashtable<String, Object> properties = new Hashtable<String, Object>();

    Registration<TestService> registration = rule.register( TestService.class, new TestServiceImpl(), properties );

    assertThat( registration ).isNotNull();
    assertThat( collectServices( TestService.class, TestServiceImpl.class ) ).hasSize( 1 );
  }

  @Test
  public void unregister() {
    Registration<TestService> registration = rule.register( TestService.class, new TestServiceImpl() );
    registration.unregister();

    assertThat( collectServices( TestService.class, TestServiceImpl.class ) ).isEmpty();
  }

  @Test
  public void statementEvaluation() throws Throwable {
    Statement base = mock( Statement.class );
    rule.register( TestService.class, new TestServiceImpl(), null );

    rule.apply( base, null ).evaluate();

    verify( base ).evaluate();
    assertThat( collectServices( TestService.class, TestServiceImpl.class ) ).isEmpty();
  }

  @Test
  public void statementEvaluationWithProblem() throws Throwable {
    Throwable expected = new Throwable();
    final Statement base = stubStatementWithEvaluationProblem( expected );
    rule.register( TestService.class, new TestServiceImpl(), null );

    Throwable actual = thrownBy( () -> rule.apply( base, null ).evaluate() );

    assertThat( actual ).isSameAs( expected );
  }

  private static Statement stubStatementWithEvaluationProblem( Throwable expected ) throws Throwable {
    Statement result = mock( Statement.class );
    doThrow( expected ).when( result ).evaluate();
    return result;
  }
}