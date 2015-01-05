package com.codeaffine.workflow;

import com.codeaffine.workflow.internal.NodeLoaderImpl;
import com.codeaffine.workflow.internal.WorkflowContextImpl;
import com.codeaffine.workflow.internal.WorkflowServiceImpl;

public class WorkflowServiceFactory {

  private final WorkflowDefinitionProvider[] definitionProviders;
  private final NodeLoader nodeLoader;

  public WorkflowServiceFactory( WorkflowDefinitionProvider ... definitionProviders ) {
    this( new NodeLoaderImpl(), definitionProviders );
  }

  public WorkflowServiceFactory( NodeLoader nodeLoader, WorkflowDefinitionProvider ... definitionProviders ) {
    this.definitionProviders = definitionProviders;
    this.nodeLoader = nodeLoader;
  }

  public WorkflowService createWorkflowService() {
    WorkflowServiceImpl result = new WorkflowServiceImpl( nodeLoader );
    for( WorkflowDefinitionProvider definitionProvider : definitionProviders ) {
      result.addWorkflowDefinition( definitionProvider );
    }
    return result;
  }

  public WorkflowContext createWorkflowContext() {
    return new WorkflowContextImpl();
  }
}