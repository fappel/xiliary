/**
 * Copyright (c) 2014 - 2017 Frank Appel
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Frank Appel - initial API and implementation
 */
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