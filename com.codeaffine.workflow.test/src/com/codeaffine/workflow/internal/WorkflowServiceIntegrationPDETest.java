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
package com.codeaffine.workflow.internal;

import static com.codeaffine.osgi.test.util.ServiceCollector.collectServices;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.codeaffine.osgi.test.util.Registration;
import com.codeaffine.osgi.test.util.ServiceRegistrationRule;
import com.codeaffine.workflow.WorkflowService;
import com.codeaffine.workflow.definition.WorkflowDefinition;
import com.codeaffine.workflow.definition.WorkflowDefinitionProvider;

public class WorkflowServiceIntegrationPDETest {

  @Rule
  public final ServiceRegistrationRule serviceRegistration = new ServiceRegistrationRule( getClass() );

  @Test
  public void workflowServiceRegistration() {
    List<WorkflowService> actual = collectWorkflowServices();

    assertThat( actual ).hasSize( 1 );
  }

  @Test
  public void workflowDefinitionProviderRegistration() {
    WorkflowDefinitionProvider provider = spy( new TestDefinitionProvider() );

    serviceRegistration.register( WorkflowDefinitionProvider.class, provider );

    verify( provider ).define( any( WorkflowDefinition.class ) );
  }

  @Test
  public void workflowDefinitionProviderDeregistration() {
    WorkflowDefinitionProvider provider = spy( new TestDefinitionProvider() );
    Registration<WorkflowDefinitionProvider> registration
      = serviceRegistration.register( WorkflowDefinitionProvider.class, provider );

    registration.unregister();

    WorkflowService workflowService = collectWorkflowServices().get( 0 );
    assertThat( workflowService.getWorkflowDefinitionIds() ).hasSize( 0 );
  }

  private static List<WorkflowService> collectWorkflowServices() {
    return collectServices( WorkflowService.class, WorkflowServiceImpl.class );
  }
}