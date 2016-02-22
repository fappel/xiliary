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
package com.codeaffine.workflow;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.codeaffine.workflow.internal.WorkflowDefinitionProviderTracker;
import com.codeaffine.workflow.internal.WorkflowServiceImpl;

public class WorkflowServiceActivator {

  private final BundleContext context;

  private ServiceRegistration<WorkflowService> workflowSerivceRegistration;
  private WorkflowDefinitionProviderTracker tracker;

  public WorkflowServiceActivator( BundleContext context ) {
    this.context = context;
  }

  public void activate() {
    WorkflowServiceImpl workflowService = new WorkflowServiceImpl();
    workflowSerivceRegistration = context.registerService( WorkflowService.class, workflowService, null );
    tracker = new WorkflowDefinitionProviderTracker( context, workflowService );
    tracker.open();
  }

  public void deactivate() {
    if( tracker != null ) {
      tracker.close();
      tracker = null;
    }
    if( workflowSerivceRegistration != null ) {
      workflowSerivceRegistration.unregister();
      workflowSerivceRegistration = null;
    }
  }
}