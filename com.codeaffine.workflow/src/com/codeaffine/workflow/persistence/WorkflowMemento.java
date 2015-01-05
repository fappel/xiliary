package com.codeaffine.workflow.persistence;

public class WorkflowMemento {

  private final OperationPointerMemento pointerMemento;
  private final WorkflowContextMemento contextMemento;

  public WorkflowMemento( OperationPointerMemento pointerMemento, WorkflowContextMemento contextMemento ) {
    this.pointerMemento = pointerMemento;
    this.contextMemento = contextMemento;
  }

  public OperationPointerMemento getPointerMemento() {
    return pointerMemento;
  }

  public WorkflowContextMemento getContextMemento() {
    return contextMemento;
  }
}