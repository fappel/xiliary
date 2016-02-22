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
package com.codeaffine.workflow.internal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.codeaffine.workflow.definition.WorkflowDefinitionProvider;

public class WorkflowDefinitionProviderTracker
  extends ServiceTracker<WorkflowDefinitionProvider, WorkflowDefinitionProvider>
{

  private final WorkflowServiceImpl workflowService;

  public WorkflowDefinitionProviderTracker( BundleContext context, WorkflowServiceImpl workflowService ) {
    super( context, WorkflowDefinitionProvider.class, null );
    this.workflowService = workflowService;
  }

  @Override
  public WorkflowDefinitionProvider addingService( ServiceReference<WorkflowDefinitionProvider> reference ) {
    WorkflowDefinitionProvider result = super.addingService( reference );
    workflowService.addWorkflowDefinition( result );
    return result;
  }

  @Override
  public void removedService(
    ServiceReference<WorkflowDefinitionProvider> reference, WorkflowDefinitionProvider service )
  {
    workflowService.removeWorkflowDefinition( service );
    super.removedService( reference, service );
  }
}