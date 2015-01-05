package com.codeaffine.workflow;

import com.codeaffine.workflow.definition.ActivityAspect;
import com.codeaffine.workflow.definition.VariableDeclaration;

public interface Workflow {

  WorkflowContext getContext();
  void copyContext( WorkflowContext workflowContext );
  <T> void defineVariable( VariableDeclaration<T> declaration, T value );
  void start();

  void addActivityAspect( ActivityAspect activityAspect );
  boolean matches( Object value );
}