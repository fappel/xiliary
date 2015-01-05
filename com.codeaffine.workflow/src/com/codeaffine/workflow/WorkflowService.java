package com.codeaffine.workflow;

import com.codeaffine.workflow.persistence.Memento;

public interface WorkflowService extends FlowEventProvider, TaskEventProvider, WorkflowFactory {
  String[] getWorkflowDefinitionIds();
  TaskList getTaskList();
  Memento save();
  void restore( Memento memento );
}