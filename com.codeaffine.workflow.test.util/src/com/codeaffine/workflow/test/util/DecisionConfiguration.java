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
package com.codeaffine.workflow.test.util;

public class DecisionConfiguration {
  
  String nodeId;
  String decision;
  String[] decisions;

  public DecisionConfiguration( String nodeId, String decision, String ... decisions ) {
    this.nodeId = nodeId;
    this.decision = decision;
    this.decisions = decisions;
  }
}