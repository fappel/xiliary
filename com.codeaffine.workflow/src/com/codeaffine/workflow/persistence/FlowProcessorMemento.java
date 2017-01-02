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

import com.codeaffine.workflow.NodeDefinition;

public class FlowProcessorMemento {

  private final NodeDefinition currentNode;
  private final boolean initialized;
  private final boolean acquired;

  public FlowProcessorMemento( NodeDefinition currentNode, boolean initialized, boolean acquired ) {
    this.currentNode = currentNode;
    this.initialized = initialized;
    this.acquired = acquired;
  }

  public NodeDefinition getCurrentNode() {
    return currentNode;
  }

  public boolean isInitialized() {
    return initialized;
  }

  public boolean isAcquired() {
    return acquired;
  }
}