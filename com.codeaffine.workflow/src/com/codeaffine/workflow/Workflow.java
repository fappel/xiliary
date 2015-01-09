package com.codeaffine.workflow;

import com.codeaffine.workflow.definition.ActivityAspect;

public interface Workflow extends Scope {

  WorkflowContext getContext();
  void copyContext( WorkflowContext workflowContext );
  void start();

  void addActivityAspect( ActivityAspect activityAspect );
  boolean matches( Object value );
}