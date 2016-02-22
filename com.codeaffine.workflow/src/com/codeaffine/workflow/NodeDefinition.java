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
package com.codeaffine.workflow;

public class NodeDefinition {

  private final String[] successors;
  private final Class<?> type;
  private final String nodeId;

  public NodeDefinition( String nodeId, Class<?> type, String ... successors ) {
    this.nodeId = nodeId;
    this.type = type;
    this.successors = successors;
  }

  public String getNodeId() {
    return nodeId;
  }

  public Class<?> getType() {
    return type;
  }

  public String[] getSuccessors() {
    return successors.clone();
  }
}