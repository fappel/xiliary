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