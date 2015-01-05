package com.codeaffine.workflow;

public interface WorkflowFactory {
  Workflow create( String definitionId );
}