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

public class WorkflowMemento {

  private final FlowProcessorMemento flowProcessorMemento;
  private final WorkflowContextMemento contextMemento;

  public WorkflowMemento( FlowProcessorMemento flowProcessorMemento, WorkflowContextMemento contextMemento ) {
    this.flowProcessorMemento = flowProcessorMemento;
    this.contextMemento = contextMemento;
  }

  public FlowProcessorMemento getFlowProcessorMemento() {
    return flowProcessorMemento;
  }

  public WorkflowContextMemento getContextMemento() {
    return contextMemento;
  }
}