package com.codeaffine.workflow.persistence;

public class WorkflowMemento {

  private final FlowProcessorMemento flowProcessorMemento;
  private final WorkflowContextMemento contextMemento;

  public WorkflowMemento( FlowProcessorMemento flowProcessorMemento, WorkflowContextMemento contextMemento ) {
    this.flowProcessorMemento = flowProcessorMemento;
    this.contextMemento = contextMemento;
  }

  public FlowProcessorMemento getFlowProcessorMemento() {
    return flowProcessorMemento;
  }

  public WorkflowContextMemento getContextMemento() {
    return contextMemento;
  }
}