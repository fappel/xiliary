package com.codeaffine.workflow;

import com.codeaffine.workflow.event.FlowEventProvider;
import com.codeaffine.workflow.event.TaskEventProvider;
import com.codeaffine.workflow.persistence.Memento;

public interface WorkflowService extends FlowEventProvider, TaskEventProvider, WorkflowFactory, Scope {
  String[] getWorkflowDefinitionIds();
  TaskList getTaskList();
  Memento save();
  void restore( Memento memento );
}