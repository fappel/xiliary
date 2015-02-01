package com.codeaffine.osgi.test.util;

import org.osgi.framework.ServiceRegistration;

public class Registration<S> {

  private final ServiceRegistration<S> serviceRegistration;
  private final ServiceRegistrationRule serviceRegistrationRule;

  Registration( ServiceRegistration<S> serviceRegistration, ServiceRegistrationRule serviceRegistrationRule ) {
    this.serviceRegistration = serviceRegistration;
    this.serviceRegistrationRule = serviceRegistrationRule;
  }

  public void unregister() {
    serviceRegistration.unregister();
    serviceRegistrationRule.remove( serviceRegistration );
  }

  public ServiceRegistration<S> getServiceRegistration() {
    return serviceRegistration;
  }
}