/**
 * Copyright (c) 2014 - 2022 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
package com.codeaffine.workflow.persistence;

import com.codeaffine.workflow.NodeLoader;
import com.codeaffine.workflow.WorkflowService;
import com.codeaffine.workflow.definition.VariableDeclaration;
import com.codeaffine.workflow.internal.ScopeContext;
import com.codeaffine.workflow.internal.WorkflowServiceImpl;

public class WorkflowServiceAdapter {

  private final WorkflowServiceImpl workflowService;

  public WorkflowServiceAdapter( WorkflowService workflowService ) {
    this.workflowService = ( WorkflowServiceImpl )workflowService;
  }

  public ClassFinder getClassFinder() {
    return workflowService.getClassFinder();
  }

  public NodeLoader getNodeLoader() {
    return workflowService.getNodeLoader();
  }

  public VariableDeclaration<?>[] getVariableDeclarations() {
    return ( ( ScopeContext )workflowService.getServiceScope() ).getVariableDeclarations();
  }
}
