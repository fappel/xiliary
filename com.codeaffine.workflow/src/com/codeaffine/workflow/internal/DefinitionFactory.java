package com.codeaffine.workflow.internal;

import com.codeaffine.workflow.definition.WorkflowDefinitionProvider;

class DefinitionFactory {

  private final WorkflowDefinitionChecker definitionChecker;

  DefinitionFactory() {
    this.definitionChecker = new WorkflowDefinitionChecker();
  }

  WorkflowDefinitionImpl defineWorkflow( WorkflowDefinitionProvider definitionProvider ) {
    WorkflowDefinitionImpl result = new WorkflowDefinitionImpl();
    definitionProvider.define( result );
    definitionChecker.checkDefinition( result );
    return result;
  }
}