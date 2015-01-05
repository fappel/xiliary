package com.codeaffine.workflow.persistence;

import com.codeaffine.workflow.Workflow;
import com.codeaffine.workflow.internal.WorkflowImpl;

public class WorkflowAdapter {

  private final WorkflowImpl workflow;

  public WorkflowAdapter( Workflow workflow ) {
    this.workflow = ( WorkflowImpl )workflow;
  }

  public String getDefinitionId() {
    return workflow.getDefinitionId();
  }

  public WorkflowMemento save() {
    return workflow.save();
  }

  public void restore( WorkflowMemento memento ) {
    workflow.restore( memento );
  }
}