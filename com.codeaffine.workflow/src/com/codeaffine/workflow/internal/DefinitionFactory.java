/**
 * Copyright (c) 2014 - 2016 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
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