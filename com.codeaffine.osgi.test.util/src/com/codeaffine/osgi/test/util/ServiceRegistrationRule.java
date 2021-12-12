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
package com.codeaffine.osgi.test.util;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

public class ServiceRegistrationRule implements TestRule {

  private final Set<ServiceRegistration<?>> registrations;
  private final BundleContext context;

  public ServiceRegistrationRule( Class<?> bundleContextProvider ) {
    context = FrameworkUtil.getBundle( bundleContextProvider ).getBundleContext();
    registrations = new HashSet<ServiceRegistration<?>>();
  }

  public <S> Registration<S> register( Class<S> type, S service ) {
    return register( type, service, null );
  }

  public <S> Registration<S> register( Class<S> type, S service, Dictionary<String, ?> properties ) {
    ServiceRegistration<S> result = context.registerService( type, service, properties );
    synchronized( registrations ) {
      registrations.add( result );
    }
    return new Registration<S>( result, this );
  }

  @Override
  public Statement apply( final Statement base, Description description ) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        try {
          base.evaluate();
        } finally {
          cleanup();
        }
      }
    };
  }

  void remove( ServiceRegistration<?> registration ) {
    synchronized( registrations ) {
      registrations.remove( registration );
    }
  }

  void cleanup() {
    synchronized( registrations ) {
      for( ServiceRegistration<?> registration : registrations ) {
        registration.unregister();
      }
      registrations.clear();
    }
  }
}